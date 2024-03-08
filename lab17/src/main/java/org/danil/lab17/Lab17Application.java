package org.danil.lab17;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


@SpringBootApplication
public class Lab17Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab17Application.class, args);
    }

//    @Bean
//    public ExecutorService executor() {
//        return Executors.newSingleThreadExecutor();
//    }
//
//    @Bean
//    public AsynchronousChannelGroup group() throws IOException {
//        return AsynchronousChannelGroup.withThreadPool(executor());
//    }

    @Bean
    int aboba() throws IOException, ExecutionException, InterruptedException {
//        final var socket = AsynchronousSocketChannel.open(group());
        final var socket = AsynchronousSocketChannel.open();
        final var host = "http://www.google.ru";
        final var imagePath = "/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
        socket.connect(new InetSocketAddress("www.google.ru", 80)).get();
        final var request = """
                GET %s HTTP/1.1
                Host: www.google.ru
                User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
                Connection: close

                """.formatted(imagePath);
        socket.write(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8))).get();

        final var buf = ByteBuffer.allocate(10_240);
        var readN = socket.read(buf).get();
        System.out.println(readN);
        buf.flip();

        final var lastChars = new CircularFifoQueue<Character>(4);

        final var headerBuffer = ByteBuffer.allocate(4096);

        final var headerEndSequences = List.of(List.of('\r', '\n', '\r', '\n'), List.of('\r', '\r'), List.of('\n', '\n'));
        while (buf.hasRemaining()) {
            final var b = buf.get();
            final var c = (char) b;
            headerBuffer.put(b);
            lastChars.add(c);
            final var isHeaderEnded = headerEndSequences.stream().anyMatch(headerEnd -> {
                final var window = lastChars.stream().skip(4 - headerEnd.size()).limit(headerEnd.size()).toList();
                System.out.format("|%s| window: %s\n", StringEscapeUtils.escapeJava("" + c), StringEscapeUtils.escapeJava(window.toString()));
                return CollectionUtils.isEqualCollection(window, headerEnd);
            });
            if (isHeaderEnded) break;
        }

        headerBuffer.flip();
        final var header = StandardCharsets.UTF_8.decode(headerBuffer).toString();
        final var headers = new HashMap<String, String>();
        final String startLine;
        {
            final var headerScanner = new Scanner(header);
            startLine = headerScanner.nextLine();

            String line;
            while(!Objects.equals(line = headerScanner.nextLine(), "")) {
                final var splited = line.split(": ");
                headers.put(splited[0], splited[1]);
            }
        }


        System.out.println(startLine + "\n\n" + headers);
        final var length = headers.get("Content-Length");

        System.out.println(buf.remaining() + " " + length);
        int lengthInt = Integer.parseInt(length);

        final var bodyBytes = new byte[lengthInt];
        var bs1 = buf.remaining();
        buf.get(bodyBytes, 0, bs1);

        final var bodyBuffer = ByteBuffer.allocate(lengthInt);
        var readN2 = socket.read(bodyBuffer).get();
        bodyBuffer.flip();
        bodyBuffer.get(bodyBytes, bs1, readN2);

        Files.write(Paths.get("C:\\Users\\Danil\\Desktop\\result.png"), bodyBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return 0;
    }
}