package org.danil.lab17;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public record Response(String startLine, Map<String, String> headers, byte[] body) {

    }

    @Builder(builderMethodName = "requiredBuilder")
    public record Request(@NotNull String host, int port, @NotNull String path, @NotNull Method method,
                          Map<String, String> headers, String body) {
        public enum Method {
            GET, POST, DELETE, PATCH, PUT, OPTIONS, HEAD, TRACE, CONNECT
        }

        public static RequestBuilder builder(@NotNull String host, int port, @NotNull String path, @NotNull Method method) {
            return requiredBuilder().host(host).port(port).path(path).method(method);
        }

        public String shortname() {
            return "Request{ " + method + ' ' + host + ':' + port +
                   Optional.of(path).map(path -> path.substring(0, Math.min(path.length(), 30))).orElse("") +
                   ' ' + Optional.ofNullable(headers).map(Object::toString).orElse("") + ' ' +
                   Optional.ofNullable(body).map(body -> body.substring(0, Math.min(body.length(), 20))).orElse("")
                   + " }";
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
            final var socket = AsynchronousSocketChannel.open(asyncChannelGroup.getGroup());
            // обертка пользовательского хендлера для асинхронного закрытия сокета при завершении запроса
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
                // дефолтные заголовки
                final Map<String, String> defaultHeaders = Map.of(
                        "Host", context.request.host(),
                        "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0",
                        "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                        "Connection", "close"
                );
                putAll(defaultHeaders);
                // добавление пользовательских заголовков
                // ! пользовательский заголовок может перезаписать дефолтные заголовки
                if (context.request.headers() != null)
                    putAll(context.request.headers());
            }};
            // Сериализация заголовков в формат HTTP
            final var headersString = headers.entrySet().stream()
                    .map((entry) -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\r\n"));
            // Шаблон HTTP запроса, поддерживается только HTTP/1.1
            final var requestString = """
                    %s %s HTTP/1.1
                    %s
                                        
                    %s""".formatted(context.request.method().toString(), context.request.path(), headersString, context.request.body());
            context.socket.write(ByteBuffer.wrap(requestString.getBytes(StandardCharsets.UTF_8)), context, new SendedCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {
            context.httpClient.logger.error("connect error: {}", context.request.shortname());
            context.handler.failed(exc, null);
        }
    }

    static class SendedCompletionHandler implements CompletionHandler<Integer, BaseContext> {
        public static final int HEADER_BUFFER_SIZE = 10_240;
        @Override
        public void completed(Integer result, BaseContext context) {
            // Обязательно нужно, чтобы весь заголовок запроса попал в буфер при первом чтении.
            // TODO: do socket.read on dataUnderflow
            final var headerBuffer = ByteBuffer.allocate(HEADER_BUFFER_SIZE);
            context.socket.read(headerBuffer, new ConnectedContext(context, headerBuffer), new HeaderReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BaseContext context) {
            context.httpClient.logger.error("send error: {}", context.request.shortname());
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

            // держим в буфере 4 последних символа для определения конца заголовка при разных разделителях
            final var lastChars = new CircularFifoQueue<Character>(4);
            // поддерживаемые варианты разделителей конца заголовка
            final var headerEndSequences = List.of(List.of('\r', '\n', '\r', '\n'), List.of('\r', '\r'), List.of('\n', '\n'));

            // буфер для каждой строки (каждого заголовка)
            final var lineBuffer = ByteBuffer.allocate(LINE_BUFFER_SIZE);
            // при чтении из сокета может не хватить данных
            // дополнительное чтение еще не предусмотрено
            while (context.headerBuffer.hasRemaining()) {
                final var b = context.headerBuffer.get();
                // чтение ASCII символа
                final var c = (char) b;
                lineBuffer.put(b);
                lastChars.add(c);
                if (c == '\n' || c == '\r') {
                    // получен перенос строки -> можно распарсить один заголовок
                    lineBuffer.flip();
                    final var line = StandardCharsets.UTF_8.decode(lineBuffer).toString().stripTrailing();
                    if (!Objects.equals(line, "")) {
                        if (startLine == null) {
                            // стартовая строка хранится отдельно
                            startLine = line;
                        } else {
                            final var splited = line.split(": ");
                            headers.put(splited[0], splited[1]);
                        }
                    }
                    lineBuffer.clear();
                }
                final var isHeaderEnded = headerEndSequences.stream().anyMatch(headerEnd -> {
                    // окно в последние 2 или 4 символов в зависимости от длины признака конца заголовка
                    final var window = lastChars.stream().skip(4 - headerEnd.size()).limit(headerEnd.size()).toList();
//                    System.out.format("|%s| window: %s\n", StringEscapeUtils.escapeJava("" + c), StringEscapeUtils.escapeJava(window.toString()));
                    // проверка на соответствие хотя бы одному признаку конца заголовка
                    return CollectionUtils.isEqualCollection(window, headerEnd);
                });
                if (isHeaderEnded) break;
            }

//            System.out.println(startLine + "\n\n" + headers);
            // проверка заголовка Content-Length
            // в HTTP/1.1 должен присутствовать всегда, он определяет длину тела ответа
            if (!headers.containsKey("Content-Length")) {
                context.baseContext().handler.failed(new RuntimeException("Content-Length does not exist"), null);
                return;
            }
            final var length = Integer.parseInt(headers.get("Content-Length"));
//            System.out.println("rem: " + context.headerBuffer.remaining() + " cont-len: " + length);

            // байтовый массив с телом ответа, выделяется единожды
            final var bodyBytes = new byte[length];

            // в буфер с заголовком могла попасть часть тела ответа, если есть то извлекаем в массив тела ответа
            final var bodyInHeaderBufferLength = context.headerBuffer.remaining();
            context.headerBuffer.get(bodyBytes, 0, bodyInHeaderBufferLength);

            // в буфере с заголовком могло оказаться тело ответа целиком, поэтому читать больше нечего, закругляемся
            if (bodyInHeaderBufferLength == length) {
                context.baseContext().handler.completed(new Response(startLine, headers, bodyBytes), null);
                return;
            }

            // буфер для неблокирующего чтения тела ответа, выделяется единожды
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
            context.baseContext.httpClient.logger.error("header read error: {}", context.baseContext.request.shortname());
            context.baseContext().handler.failed(exc, null);
        }
    }

    static class BodyReadCompletionHandler implements CompletionHandler<Integer, BodyContext> {

        @Override
        public void completed(Integer readN, BodyContext context) {
            // переворачиваем буфер и извлекаем все, что есть в массив ответа
            context.bodyBuffer.flip();
            context.bodyBuffer.get(context.bodyBytes, context.readCount, readN);
            context.bodyBuffer.clear();

            // новое количество прочитанных байт
            final var readCount2 = context.readCount + readN;
            context.baseContext().httpClient.logger.info("Прочитано {} {}", readCount2, context.baseContext().request.shortname());

            // если прочитали все, что требовалось, то закругляемся
            if (readCount2 == context.bodyLength) {
                context.baseContext().handler.completed(new Response(context.headerContext().startLine, context.headerContext().headers, context.bodyBytes), null);
                return;
            }

            final var rateLimit = context.baseContext().options().rateLimitKbS;
            if (rateLimit != null) {
                final var now = new Date();
                final var timeElapsed = now.getTime() - context.lastReadAt().getTime();
                final var expectedBytes = rateLimit * timeElapsed;
                // по разности времени и количества прочитанных байт
                // определяем необходимость приостановить чтение на некоторое время
                if (readN > expectedBytes) {
                    // не знаю почему, но нужно умножать задержку на 2, выяснил экспериментально
                    final var delayMs = (readN - expectedBytes) / rateLimit * 2;
                    context.baseContext().httpClient.logger.info("Загрузка приостановлена на {}мс {}", delayMs, context.baseContext().request.shortname());
                    // не знаю точного устройства работы delayedExecutor, но надеюсь он тоже неблокирующий
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
            // если ограничение скорости не указано или скорость не превышено,
            // то пересобираем контекст и читаем еще разок
            context.baseContext().socket.read(context.bodyBuffer,
                    context.toBuilder()
                            .readCount(readCount2)
                            .lastReadAt(new Date())
                            .build(),
                    new BodyReadCompletionHandler());
        }

        @Override
        public void failed(Throwable exc, BodyContext context) {
            context.baseContext().httpClient.logger.error("body read {} error: {}", context.readCount, context.baseContext().request.shortname());
            context.baseContext().handler.failed(exc, null);
        }
    }
}
