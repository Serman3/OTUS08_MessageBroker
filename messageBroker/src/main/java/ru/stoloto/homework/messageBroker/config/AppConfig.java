package ru.stoloto.homework.messageBroker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class AppConfig {

    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(1);
    }
}
