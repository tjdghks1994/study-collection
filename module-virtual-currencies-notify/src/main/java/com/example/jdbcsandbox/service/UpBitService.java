package com.example.jdbcsandbox.service;

import com.example.jdbcsandbox.controller.MinuteCandleRequest;
import com.example.jdbcsandbox.http.HttpClient;
import com.example.jdbcsandbox.http.UpBitFeignClient;
import com.example.jdbcsandbox.http.UpBitMinuteCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UpBitService {
    private final HttpClient httpClient;
    private final UpBitFeignClient upBitFeignClient;
    private final ObjectMapper objectMapper;

    public UpBitService(HttpClient httpClient, UpBitFeignClient upBitFeignClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.upBitFeignClient = upBitFeignClient;
        this.objectMapper = new ObjectMapper();
    }

    public List<MinuteCandle> getCandles(int unit, MinuteCandleRequest request) throws JsonProcessingException {
        // uri
        String uri = UriComponentsBuilder.fromUriString("https://api.upbit.com/")
                .path("v1/candles/minutes/" + unit)
                .queryParam("market", request.getMarket())
                .queryParam("count", request.getCount())
                .build()
                .toUriString();

        // header
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        // call
        String result = httpClient.getData(uri, HttpMethod.GET, httpHeaders);
        String feignResult = upBitFeignClient.getMinuteCandle(unit, request.getMarket(), request.getCount());

        List<UpBitMinuteCandle> upBitMinuteCandles =
                objectMapper.readValue(result, new TypeReference<List<UpBitMinuteCandle>>() {});
        List<UpBitMinuteCandle> feignUpBitMinuteCandles =
                objectMapper.readValue(feignResult, new TypeReference<List<UpBitMinuteCandle>>() {});

        return feignUpBitMinuteCandles.stream().map(it -> MinuteCandle.builder()
                .market(it.getMarket())
                .candleDateTimeUtc(it.getCandleDateTimeUtc())
                .candleDateTimeKst(it.getCandleDateTimeKst())
                .openingPrice(it.getOpeningPrice())
                .highPrice(it.getHighPrice())
                .lowPrice(it.getLowPrice())
                .tradePrice(it.getTradePrice())
                .timestamp(it.getTimestamp())
                .candleAccTradePrice(it.getCandleAccTradePrice())
                .candleAccTradeVolume(it.getCandleAccTradeVolume())
                .unit(it.getUnit())
                .build()).collect(Collectors.toList());
    }
}
