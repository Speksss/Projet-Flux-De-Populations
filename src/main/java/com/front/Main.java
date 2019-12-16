package com.front;

import com.front.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
//    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//        return args -> {
//            User user = restTemplate.getForObject("http://35.206.157.216:8080//login?email=admin@gmail.com&password=admin", User.class);
//            log.info(user.toString());
//        };
//    }
}