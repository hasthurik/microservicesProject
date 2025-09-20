package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class AppAccount
{
    public static void main(String[] args) {
        SpringApplication.run(AppAccount.class, args);
    }
}
