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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HttpClient {
    private final AsyncChannelGroup asyncChannelGroup;
    private final Executor executor;

    public record Response(String startLine, Map<String, String> headers, byte[] body) {

    }

    @Builder(builderMethodName = "requiredBuilder")
    public record Request(@NotNull String host, int port, @NotNull String path, @NotNull Method method, Map<String, String> headers, String body) {
        public enum Method {
            GET, POST, DELETE, PATCH, PUT, OPTIONS, HEAD, TRACE, CONNECT
        }
        public static RequestBuilder builder(@NotNull String host, int port, @NotNull String path, @NotNull Method method) {
            return requiredBuilder().host(host).port(port).path(path).method(method);
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Options {
        @Builder.Default
        Long rateLimitKbS = null;
    }

    protected record BaseContext(@NotNull Request request, @NotNull CompletionHandler<Response, Object> handler,
                       @NotNull Options options,
                       @NotNull AsynchronousSocketChannel socket, @NotNull HttpClient httpClient) {
    }

    protected record ConnectedContext(@NotNull BaseContext baseContext, @NotNull ByteBuffer headerBuffer) {
    }

    protected record HeaderContext(@NotNull ConnectedContext connectedContext, @NotNull String startLine,
                         @NotNull Map<String, String> headers) {
        BaseContext baseContext() {
            return connectedContext.baseContext();
        }
    }

    @Builder(toBuilder = true)
    protected record BodyContext(@NotNull HeaderContext headerContext, int readCount, int bodyLength,
                       @NotNull Date lastReadAt,
                       @NotNull ByteBuffer bodyBuffer, byte @NotNull [] bodyBytes) {
        BaseContext baseContext() {
            return headerContext.baseContext();
        }
    }

    public void fetch(@NotNull Request request, @NotNull CompletionHandler<Response, Object> responseHandler, @NotNull Options options) {
        try {
            // resource closing is managed in complete handler
            final var socket = AsynchronousSocketChannel.open(asyncChannelGroup.getGroup());
            final var handler = new CompletionHandler<Response, Object>() {
                @Override
                public void completed(Response response, Object attachment) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        responseHandler.failed(e, attachment);
                    } finally {
                        responseHandler.completed(response, attachment);
                    }
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        responseHandler.failed(e.initCause(exc), attachment);
                    } finally {
                        responseHandler.failed(exc, attachment);
                    }
                }
            };
            final var baseContext = new BaseContext(request, handler, options, socket, this);
            socket.connect(new InetSocketAddress(request.host(), request.port()), baseContext, new ConnectedCompletionHandler());
        } catch (Throwable th) {
            responseHandler.failed(th, null);
        }
    }


    static class ConnectedCompletionHandler implements CompletionHandler<Void, BaseContext> {

        @Override
        public void completed(Void result, BaseContext context) {
            final var headers = new HashMap<>() {{
                final Map<String, String> defaultHeaders = Map.of(
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
                    %s %s HTTP/1.1
                    %s
                                        
                    %s""".formatted(context.request.method().toString(), context.request.path(), headersString, context.request.body());
            context.socket.write(ByteBuffer.wrap(requestString.getBytes(StandardCharsets.UTF_8)), context, new SendedCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {
            System.out.println("connect error");
            context.handler.failed(exc, null);
        }
    }

    static class SendedCompletionHandler implements CompletionHandler<Integer, BaseContext> {

        @Override
        public void completed(Integer result, BaseContext context) {
            // Обязательно нужно, чтобы весь заголовок запроса попал в буфер.
            // TODO: do socket.read on dataOverflow
            final var headerBuffer = ByteBuffer.allocate(10_240);
            context.socket.read(headerBuffer, new ConnectedContext(context, headerBuffer), new HeaderReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {
            System.out.println("send error");
            context.handler.failed(exc, null);
        }
    }

    static class HeaderReadCompletionHandler implements CompletionHandler<Integer, ConnectedContext> {
        private static final int LINE_BUFFER_SIZE = 4096;
        public static final int BODY_BUFFER_SIZE = 256_000;
        public static final int INITIAL_HEADERS_MAP_CAPACITY = 50;

        @Override
        public void completed(Integer result, ConnectedContext context) {
            context.headerBuffer.flip();

            final var headers = new HashMap<String, String>(INITIAL_HEADERS_MAP_CAPACITY);
            String startLine = null;

            final var lastChars = new CircularFifoQueue<Character>(4);
            final var headerEndSequences = List.of(List.of('\r', '\n', '\r', '\n'), List.of('\r', '\r'), List.of('\n', '\n'));
            final var lineBuffer = ByteBuffer.allocate(LINE_BUFFER_SIZE);
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
                            headers.put(splited[0], splited[1]);
                        }
                    }
                    lineBuffer.clear();
                }
                final var isHeaderEnded = headerEndSequences.stream().anyMatch(headerEnd -> {
                    final var window = lastChars.stream().skip(4 - headerEnd.size()).limit(headerEnd.size()).toList();
//                    System.out.format("|%s| window: %s\n", StringEscapeUtils.escapeJava("" + c), StringEscapeUtils.escapeJava(window.toString()));
                    return CollectionUtils.isEqualCollection(window, headerEnd);
                });
                if (isHeaderEnded) break;
            }

//            System.out.println(startLine + "\n\n" + headers);
            if(!headers.containsKey("Content-Length")) {
                context.baseContext().handler.failed(new RuntimeException("Content-Length does not exist"), null);
                return;
            }
            final var length = Integer.parseInt(headers.get("Content-Length"));
//            System.out.println("rem: " + context.headerBuffer.remaining() + " cont-len: " + length);

            final var bodyBytes = new byte[length];

            final var bodyInHeaderBufferLength = context.headerBuffer.remaining();
            context.headerBuffer.get(bodyBytes, 0, bodyInHeaderBufferLength);

            if (bodyInHeaderBufferLength == length) {
                context.baseContext().handler.completed(new Response(startLine, headers, bodyBytes), null);
                return;
            }

            final var bodyBuffer = ByteBuffer.allocate(BODY_BUFFER_SIZE);
//            System.out.println("start readCount " + bodyInHeaderBufferLength);
            final var headerContext = new HeaderContext(context, startLine, headers);
            context.baseContext.socket.read(bodyBuffer,
                    BodyContext.builder()
                            .headerContext(headerContext)
                            .readCount(bodyInHeaderBufferLength)
                            .bodyLength(length)
                            .bodyBuffer(bodyBuffer)
                            .bodyBytes(bodyBytes)
                            .lastReadAt(new Date(0))
                            .build(),
                    new BodyReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, ConnectedContext context) {
            System.out.println("header read error");
            context.baseContext().handler.failed(exc, null);
        }
    }

    static class BodyReadCompletionHandler implements CompletionHandler<Integer, BodyContext> {

        @Override
        public void completed(Integer readN, BodyContext context) {
            context.bodyBuffer.flip();
            context.bodyBuffer.get(context.bodyBytes, context.readCount, readN);
            context.bodyBuffer.clear();

            final var readCount2 = context.readCount + readN;
            System.out.println("readCount: " + readCount2);

            if (readCount2 == context.bodyLength) {
                context.baseContext().handler.completed(new Response(context.headerContext().startLine, context.headerContext().headers, context.bodyBytes), null);
                return;
            }

            final var rateLimit = context.baseContext().options().rateLimitKbS;
            if (rateLimit != null) {
                final var now = new Date();
                final var timeElapsed = now.getTime() - context.lastReadAt().getTime();
                final var expectedBytes = rateLimit * timeElapsed;
                if (readN > expectedBytes) {
                    final var delayMs = (readN - expectedBytes) / rateLimit;
                    System.out.println("delayed for: " + delayMs);
                    CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS, context.baseContext().httpClient.executor)
                            .execute(() -> context.baseContext().socket.read(context.bodyBuffer,
                                    context.toBuilder()
                                            .readCount(readCount2)
                                            .lastReadAt(now)
                                            .build(),
                                    new BodyReadCompletionHandler())
                            );
                    return;
                }
            }
            context.baseContext().socket.read(context.bodyBuffer,
                    context.toBuilder()
                            .readCount(readCount2)
                            .lastReadAt(new Date())
                            .build(),
                    new BodyReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BodyContext context) {
            System.out.println("read 1 error");
            context.baseContext().handler.failed(exc, null);
        }
    }
}
