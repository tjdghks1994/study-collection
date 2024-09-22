package com.example.jdbcsandbox.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {
    private final RestTemplate restTemplate;

    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getData(String uri, HttpMethod httpMethod, HttpHeaders headers) {
        // 외부 api 호출
        return restTemplate.exchange(
                uri,
                httpMethod,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<String>() {
                }
        ).getBody();
    }
}
