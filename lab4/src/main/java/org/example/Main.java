package org.example;

import org.example.models.TerminalImpl;
import org.example.models.TerminalServerImpl;
import org.example.models.TerminalServerLockImpl;
import org.example.views.ConsoleTerminal;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final var consoleTerminal = new ConsoleTerminal(
                new TerminalImpl(
                        new TerminalServerImpl(
                                new TerminalServerLockImpl("1234", 10)
                        )
                )
        );
        consoleTerminal.run();
    }

    public static void task2() {
        /*
         https://jsonplaceholder.typicode.com/todos/1
         https://yandex.ru/search/?text=make+request+with+java.net
         */
        final var scanner = new Scanner(System.in);
        System.out.println("Receive content from URL");
        while (true) {
            System.out.println("Enter URL");
            final var url = scanner.nextLine();
            try {
                var response = readContent(url);
                if(response.length() > 100)
                    response = response.substring(0, 300) + "...";
                System.out.println("Response: " + response);
                break;
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Internal Error: " + e.getMessage());
            }
        }
    }

    public static String readContent(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        InputStream responseStream = connection.getInputStream();
        return new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}