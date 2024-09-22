package com.example.jdbcsandbox.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MinuteCandle {
    private String market;
    private String candleDateTimeUtc;
    private String candleDateTimeKst;
    private String openingPrice;
    private String highPrice;
    private String lowPrice;
    private String tradePrice;
    private String timestamp;
    private String candleAccTradePrice;
    private String candleAccTradeVolume;
    private String unit;
}
