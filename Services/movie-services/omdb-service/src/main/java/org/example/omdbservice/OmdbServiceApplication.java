package org.example.omdbservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OmdbServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmdbServiceApplication.class, args);

    }
}
