package com.example.cryptoautomation.http;

import com.example.cryptoautomation.exception.UpBitClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class HttpClient {
    private final RestTemplate restTemplate;

    public String execute(String uri, HttpMethod httpMethod, HttpHeaders httpHeaders) {
        try {
            return restTemplate.exchange(
                    uri,
                    httpMethod,
                    new HttpEntity<>(httpHeaders),
                    new ParameterizedTypeReference<String>() {}
            ).getBody();
        } catch (RestClientException e) {
            throw new UpBitClientException(e.getMessage());
        }
    }
}
