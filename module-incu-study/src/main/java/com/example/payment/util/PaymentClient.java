package com.example.payment.util;

import com.example.payment.service.PortOneRequestUrl;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClient {

//    private final RestClient restClient;
//
//    @Value("${payment.imp-key}")
//    private String impKey;
//
//    @Value("${payment.imp-secret}")
//    private String impSecret;
//
//    private static final String BASE_URL = "https://api.iamport.kr";
//
//    /**
//     * Get Access Token
//     */
//    public Map getAccessToken() {
//        String url = BASE_URL + PortOneRequestUrl.ACCESS_TOKEN_URL.getUrl();
//        try {
//            // Construct the request body
//            String requestBody = String.format("{\"imp_key\": \"%s\", \"imp_secret\": \"%s\"}", impKey, impSecret);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // Send POST request
//            return restClient.post()
//                    .uri(url)
//                    .headers(h -> h.addAll(headers))
//                    .body(requestBody)
//                    .retrieve()
//                    .body(Map.class);
//        } catch (RestClientException e) {
//            throw new RuntimeException("Failed to get access token", e);
//        }
//    }
}
