package org.example.movieservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = {"org.example.movieservice"})
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "org.example.common"
)
@PropertySources({
        @PropertySource("classpath:common-${spring.profiles.active}.properties")
})
public class MovieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApplication.class, args);
    }

}
