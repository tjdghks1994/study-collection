package com.example.cryptoautomation.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpBitHttpClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UpBitTickerDto getTickerByMarket(String market) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("accept", "application/json");

            String result = httpClient.execute(
                    "https://api.upbit.com/v1/ticker?markets=" + market,
                    HttpMethod.GET,
                    httpHeaders
            );
            List<UpBitTickerDto> upBitTickerDtos = objectMapper
                    .readValue(result, new TypeReference<List<UpBitTickerDto>>() {});

            return upBitTickerDtos.stream().findFirst().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
