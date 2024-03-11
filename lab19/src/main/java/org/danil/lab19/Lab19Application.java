package org.danil.lab19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Lab19Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab19Application.class, args);
    }

}
