package com.example.jdbcsandbox.service;

import com.example.jdbcsandbox.http.UpBitFeignClient;
import com.example.jdbcsandbox.http.UpBitMinuteCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpBitMarketService {
    private final UpBitFeignClient upBitFeignClient;
    public void callUpBit() {
        System.out.println(">>>> Call UpBit");
    }

    public void callUpBitMinuteCandle(int unit, String market) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String result = upBitFeignClient.getMinuteCandle(unit, market, 2);
        List<UpBitMinuteCandle> list =
                objectMapper.readValue(result, new TypeReference<List<UpBitMinuteCandle>>() {});

        list.forEach(System.out::println);
    }
}
