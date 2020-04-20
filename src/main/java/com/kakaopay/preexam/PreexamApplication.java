package com.kakaopay.preexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass=true)
public class PreexamApplication {
    public static void main(String[] args) {
        SpringApplication.run(PreexamApplication.class, args);
    }
}
