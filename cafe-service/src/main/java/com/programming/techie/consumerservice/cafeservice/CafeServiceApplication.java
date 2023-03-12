package com.programming.techie.consumerservice.cafeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CafeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CafeServiceApplication.class, args);
    }
}