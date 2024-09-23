package com.example.jdbcsandbox.http;

import com.example.jdbcsandbox.exception.UpBitClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class HttpClient {
    private final RestTemplate restTemplate;

    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getData(String uri, HttpMethod httpMethod, HttpHeaders headers) {
        try {
            // 외부 api 호출
            return restTemplate.exchange(
                    uri,
                    httpMethod,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<String>() {
                    }
            ).getBody();
        } catch (RestClientException e) {
            log.error("외부 API 통신 중 오류 발생", e.getMessage());
            throw new UpBitClientException(e.getMessage());
        }

    }
}
