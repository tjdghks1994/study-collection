package com.example.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestClient restClient(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));    // 서버에 연결되기 까지의 소요 시간 설정
        requestFactory.setReadTimeout(Duration.ofSeconds(3));       // 서버와 연결되어 서버로부터 Response 데이터를 받기까지 대기하는 시간 설정

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}
