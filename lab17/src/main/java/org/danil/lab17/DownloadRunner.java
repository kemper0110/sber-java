package org.danil.lab17;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
@Component
public class DownloadRunner implements CommandLineRunner {
    private final HttpClient httpClient;
    @Override
    public void run(String... args) throws Exception {
        final var host = "www.google.ru";
//        final var imagePath = "/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
        final var image400Path = "/logos/doodles/2024/jose-guadalupe-posadas-172nd-birthday-6753651837110182.3-2x.png";
        final var host2 = "www.gstatic.com";
        final var image2mbPath = "/marketing-cms/79/f2/7ab8503042a798d9fd32ad440d24/screenshot-2024-02-01-at-3-37-00-pm.png";

        final var blockingWait = new CountDownLatch(1);

        httpClient.fetch(new HttpClient.Request(host2, image2mbPath, null), SimpleCompletionHandler.<HttpClient.Response>builder()
                        .completed(response -> {
                            try {
                                Files.write(Paths.get("C:\\Users\\Danil\\Desktop\\result.png"), response.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } finally {
                                blockingWait.countDown();
                            }
                        })
                        .failed(th -> {
                            System.out.println("Top level error " + th + " " + th.getMessage());
                            blockingWait.countDown();
                        })
                .build());

        blockingWait.await();
        System.out.println("done");
    }
}
