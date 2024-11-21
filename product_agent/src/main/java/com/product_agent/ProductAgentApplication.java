package com.product_agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProductAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAgentApplication.class, args);
    }

}
