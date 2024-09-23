package com.example.jdbcsandbox.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "minute-candle", url = "https://api.upbit.com/")
public interface UpBitFeignClient {
    @GetMapping("v1/candles/minutes/{unit}")
    String getMinuteCandle(
            @PathVariable Integer unit,
            @RequestParam String market,
            @RequestParam int count
    );
}
