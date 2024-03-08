package org.danil.lab17;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HttpClient {
    private final AsyncChannelGroup asyncChannelGroup;

    public record Response(String startLine, Map<String, String> headers, byte[] body) {

    }

    public record Request(@NotNull String host, @NotNull String path, Map<String, String> headers) {

    }

    @Builder
    @Getter
    @Setter
    public static class Options {
        @Builder.Default
        Long rateLimitKbS = null;
    }

    @Builder(toBuilder = true)
    record BaseContext(@NotNull Request request, @NotNull SimpleCompletionHandler<Response> handler,
                       @NotNull Options options,
                       @NotNull AsynchronousSocketChannel socket) {
    }

    @Builder(toBuilder = true)
    record ConnectedContext(@NotNull BaseContext baseContext, ByteBuffer headerBuffer) {
    }

    @Builder(toBuilder = true)
    record HeaderContext(@NotNull ConnectedContext connectedContext, String startLine, Map<String, String> headers) {
        BaseContext baseContext() {
            return connectedContext.baseContext();
        }
    }

    @Builder(toBuilder = true)
    record BodyContext(HeaderContext headerContext, int readCount, int bodyLength, ByteBuffer bodyBuffer,
                       byte[] bodyBytes) {
        BaseContext baseContext() {
            return headerContext.baseContext();
        }
    }

    public void fetch(@NotNull Request request, @NotNull SimpleCompletionHandler<Response> responseHandler, @NotNull Options options) {
        try {
            // resource closing is managed in complete handler
            final var socket = AsynchronousSocketChannel.open(asyncChannelGroup.getGroup());
            final var handler = SimpleCompletionHandler.<Response>builder()
                    .completed(response -> {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            responseHandler.completed.accept(response);
                        }
                    })
                    .failed(th -> {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            responseHandler.failed.accept(th);
                        }
                    })
                    .build();
            final var baseContext = new BaseContext(request, handler, options, socket);
            socket.connect(new InetSocketAddress(request.host(), 80), baseContext, new ConnectedCompletionHandler());
        } catch (Throwable th) {
            responseHandler.failed.accept(th);
        }
    }


    static class ConnectedCompletionHandler implements CompletionHandler<Void, BaseContext> {

        @Override
        public void completed(Void result, BaseContext context) {
            final var headers = new HashMap<>() {{
                final var defaultHeaders = Map.of(
                        "Host", context.request.host(),
                        "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0",
                        "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                        "Connection", "close"
                );
                putAll(defaultHeaders);
                if (context.request.headers() != null)
                    putAll(context.request.headers());
            }};
            final var headersString = headers.entrySet().stream()
                    .map((entry) -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\r\n"));
            final var requestString = """
                                GET %s HTTP/1.1
                                %s
                                                    
                                """.formatted(context.request.path(), headersString);
            context.socket.write(ByteBuffer.wrap(requestString.getBytes(StandardCharsets.UTF_8)), context, new SendedCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {

        }
    }

    static class SendedCompletionHandler implements CompletionHandler<Integer, BaseContext> {

        @Override
        public void completed(Integer result, BaseContext context) {
            // Обязательно нужно, чтобы весь заголовок запроса попал в буфер.
            final var headerBuffer = ByteBuffer.allocate(10_240);
            context.socket.read(headerBuffer, new ConnectedContext(context, headerBuffer), new HeaderReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {

        }
    }

    static class HeaderReadCompletionHandler implements CompletionHandler<Integer, ConnectedContext> {

        @Override
        public void completed(Integer result, ConnectedContext context) {
            context.headerBuffer.flip();

            final var responseHeaders = new HashMap<String, String>(50);
            String startLine = null;
            final var lastChars = new CircularFifoQueue<Character>(4);
            final var headerEndSequences = List.of(List.of('\r', '\n', '\r', '\n'), List.of('\r', '\r'), List.of('\n', '\n'));
            final var lineBuffer = ByteBuffer.allocate(4096);
            while (context.headerBuffer.hasRemaining()) {
                final var b = context.headerBuffer.get();
                final var c = (char) b;
                lineBuffer.put(b);
                lastChars.add(c);
                if (c == '\n' || c == '\r') {
                    lineBuffer.flip();
                    final var line = StandardCharsets.UTF_8.decode(lineBuffer).toString().stripTrailing();
                    if (!Objects.equals(line, "")) {
                        if (startLine == null) {
                            startLine = line;
                        } else {
                            final var splited = line.split(": ");
                            responseHeaders.put(splited[0], splited[1]);
                        }
                    }
                    lineBuffer.clear();
                }
                final var isHeaderEnded = headerEndSequences.stream().anyMatch(headerEnd -> {
                    final var window = lastChars.stream().skip(4 - headerEnd.size()).limit(headerEnd.size()).toList();
//                                                    System.out.format("|%s| window: %s\n", StringEscapeUtils.escapeJava("" + c), StringEscapeUtils.escapeJava(window.toString()));
                    return CollectionUtils.isEqualCollection(window, headerEnd);
                });
                if (isHeaderEnded) break;
            }

//                                        System.out.println(startLine + "\n\n" + responseHeaders);
            final var length = Integer.parseInt(responseHeaders.get("Content-Length"));

            System.out.println("rem: " + context.headerBuffer.remaining() + " cont-len: " + length);

            final var bodyBytes = new byte[length];

            final var bodyInHeaderBufferLength = context.headerBuffer.remaining();
            context.headerBuffer.get(bodyBytes, 0, bodyInHeaderBufferLength);

            final var bodyBuffer = ByteBuffer.allocate(length);

            final var readCount = bodyInHeaderBufferLength;
            System.out.println("start readCount " + readCount);

            final var headerContext = new HeaderContext(context, startLine, responseHeaders);
            context.baseContext.socket.read(bodyBuffer,
                    BodyContext.builder()
                            .headerContext(headerContext)
                            .readCount(bodyInHeaderBufferLength)
                            .bodyLength(length)
                            .bodyBuffer(bodyBuffer)
                            .bodyBytes(bodyBytes)
                            .build(),
                    new BodyReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, ConnectedContext context) {

        }
    }

    static class BodyReadCompletionHandler implements CompletionHandler<Integer, BodyContext> {

        @Override
        public void completed(Integer readN, BodyContext context) {
            context.bodyBuffer.flip();
            context.bodyBuffer.get(context.bodyBytes, context.readCount, readN);
            context.bodyBuffer.clear();

            final var readCount2 = context.readCount + readN;
            System.out.println("lambda readCount " + readCount2);

            if (readCount2 == context.bodyLength) {
                context.baseContext().handler.completed.accept(new Response(context.headerContext().startLine, context.headerContext().headers, context.bodyBytes));
            } else {
                context.baseContext().socket.read(context.bodyBuffer,
                        context.toBuilder().readCount(readCount2).build(),
                        new BodyReadCompletionHandler());
            }
        }

        @Override
        public void failed(Throwable exc, BodyContext context) {
            System.out.println("read 1");
            context.baseContext().handler.failed.accept(exc);
        }
    }
}
