package org.danil.lab17;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
@Component
public class DownloadRunner implements CommandLineRunner {
    private final HttpClient httpClient;
    private final Executor executor;

    @Override
    public void run(String... args) throws Exception {


        final var requests = List.of(
//                new HttpClient.Request("www.google.ru", "/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png", null)
//                new HttpClient.Request("www.google.ru", "/logos/doodles/2024/jose-guadalupe-posadas-172nd-birthday-6753651837110182.3-2x.png", null),
//                new HttpClient.Request("www.gstatic.com", "/marketing-cms/79/f2/7ab8503042a798d9fd32ad440d24/screenshot-2024-02-01-at-3-37-00-pm.png", null)
//                new HttpClient.Request("redirector.gvt1.com", "/edgedl/android/studio/install/2023.2.1.23/android-studio-2023.2.1.23-windows.exe", null),
//                new HttpClient.Request("r2---sn-gvnuxaxjvh-gv8l.gvt1.com", "/edgedl/android/studio/install/2023.2.1.23/android-studio-2023.2.1.23-windows.exe?cms_redirect=yes&amp;mh=YU&amp;mip=93.178.109.94&amp;mm=28&amp;mn=sn-gvnuxaxjvh-gv8l&amp;ms=nvh&amp;mt=1709921221&amp;mv=u&amp;mvi=2&amp;pl=22&amp;rmhost=r7---sn-gvnuxaxjvh-gv8l.gvt1.com&amp;shardbypass=sd&amp;smhost=r5---sn-gvnuxaxjvh-gv8z.gvt1.com", null)
//                new HttpClient.Request("sample-videos.com", "/img/Sample-jpg-image-30mb.jpg", null)
                HttpClient.Request.builder("lh3.googleusercontent.com", 80, "/9VPwbk2TYb6p0blX5ck39Y_dXPZit_1c7_WDRsRZalw3pvq0VqQRd9aUGx_xIRu_M_AF-vVzNq2rxvAltSn1hnX39dlTxO2WA3WC5JmpoBFQBSb6NV9sQQ=s0", HttpClient.Request.Method.GET).build(),
                HttpClient.Request.builder("lh3.googleusercontent.com", 80, "/9AZmPgQ5MGCZectYtVjkVngmJ7lFUyhv7gzKRY2PO1UAG9OxEXqf9ew_uhJ5XApoaOaStcDVobFrkB55NW_Da58Ofp7P1qI6bQzDI9inxSL2hC-3fhqa=s0", HttpClient.Request.Method.GET).build()
        );

        final var blockingWait = new CountDownLatch(requests.size());

        final var startTime = System.currentTimeMillis();
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
            }, HttpClient.Options.builder().rateLimitKbS(500L).build());
        }

        System.out.println("wating ");
        blockingWait.await();
        executor.shutdown();
    }
}
