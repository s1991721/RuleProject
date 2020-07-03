package com.ljf.ruleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RuleprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleprojectApplication.class, args);
    }

}
