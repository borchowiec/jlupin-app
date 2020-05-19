package com.example;

import com.example.configuration.RestApiSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootApplicationStarter {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RestApiSpringConfiguration.class, args);
    }
}

