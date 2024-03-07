package org.danil.lab18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Lab18Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab18Application.class, args);
    }

}
