package com.wt.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wt.springboot"})
public class AopApplication {
    public static void main(String[] args){
        SpringApplication.run(AopApplication.class);
    }
}
