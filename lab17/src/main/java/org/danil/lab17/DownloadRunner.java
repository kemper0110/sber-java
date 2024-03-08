package org.danil.lab17;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
@Component
public class DownloadRunner implements CommandLineRunner {
    private final HttpClient httpClient;

    @Override
    public void run(String... args) throws Exception {


        final var requests = List.of(
                new HttpClient.Request("www.google.ru", "/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png", null),
                new HttpClient.Request("www.google.ru", "/logos/doodles/2024/jose-guadalupe-posadas-172nd-birthday-6753651837110182.3-2x.png", null),
                new HttpClient.Request("www.gstatic.com", "/marketing-cms/79/f2/7ab8503042a798d9fd32ad440d24/screenshot-2024-02-01-at-3-37-00-pm.png", null)
        );

        final var blockingWait = new CountDownLatch(requests.size());

        int i = 0;
        for (HttpClient.Request req : requests) {
            i++;
            int finalI = i;
            httpClient.fetch(req, new CompletionHandler<>() {
                @Override
                public void completed(HttpClient.Response response, Object attachment) {
                    try {
                        Files.write(Paths.get("C:\\Users\\Danil\\Desktop\\image_%d.png".formatted(finalI)),
                                response.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
            }, HttpClient.Options.builder().build());
        }

        blockingWait.await();
        System.out.println("done");
    }
}
