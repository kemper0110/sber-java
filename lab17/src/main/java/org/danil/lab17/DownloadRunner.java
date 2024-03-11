package org.danil.lab17;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
@Component
public class DownloadRunner implements CommandLineRunner {
    private final HttpClient httpClient;
    private final Executor executor;

    protected List<HttpClient.Request> readRequests(String filename) throws IOException, URISyntaxException {
        final var url = getClass().getClassLoader().getResource(filename);
        Objects.requireNonNull(url);
        try (final var lines = Files.lines(Paths.get(url.toURI()))) {
            final var it = lines.iterator();
            final var header = Arrays.asList(it.next().split("\\s*,\\s*"));

            final var requests = new ArrayList<HttpClient.Request>(10);
            it.forEachRemaining(line -> {
                final var splited = line.split("\\s*,\\s*");
                if (splited.length != header.size())
                    throw new IllegalArgumentException();
                requests.add(
                        HttpClient.Request
                                .builder(
                                        splited[header.indexOf("host")],
                                        Integer.parseInt(splited[header.indexOf("port")]),
                                        splited[header.indexOf("path")],
                                        HttpClient.Request.Method.valueOf(splited[header.indexOf("method")])
                                )
                                .build()
                );
            });
            return requests;
        }
    }

    @Override
    public void run(String... args) throws Exception {

        final var requests = readRequests("downloads.csv");

        final var blockingWait = new CountDownLatch(requests.size());

        final var startTime = System.currentTimeMillis();
        int i = 0;
        for (HttpClient.Request req : requests) {
            i++;
            int finalI = i;
            httpClient.fetch(req, new CompletionHandler<>() {
                @Override
                public void completed(HttpClient.Response response, Object attachment) {
                    final var mimeType = MimeType.valueOf(response.headers().get("Content-Type"));
                    try {
                        Files.write(Paths.get("file_%d.%s".formatted(finalI, mimeType.getSubtype())),
                                response.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("done for " + (System.currentTimeMillis() - startTime));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        blockingWait.countDown();
                    }
                }

                @Override
                public void failed(Throwable th, Object attachment) {
                    System.out.println("Top level error " + th + " " + th.getMessage());
                    blockingWait.countDown();
                }
            },
                    // выставил ограничение скорости
                    HttpClient.Options.builder().rateLimitKbS(500L).build());
        }

        System.out.println("wating ");
        blockingWait.await();
        executor.shutdown();
    }
}
