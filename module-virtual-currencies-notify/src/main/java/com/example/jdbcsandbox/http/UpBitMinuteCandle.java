package com.example.jdbcsandbox.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpBitMinuteCandle {
    private String market;
    @JsonProperty("candle_date_time_utc")
    private String candleDateTimeUtc;
    @JsonProperty("candle_date_time_kst")
    private String candleDateTimeKst;
    @JsonProperty("opening_price")
    private String openingPrice;
    @JsonProperty("high_price")
    private String highPrice;
    @JsonProperty("low_price")
    private String lowPrice;
    @JsonProperty("trade_price")
    private String tradePrice;
    private String timestamp;
    @JsonProperty("candle_acc_trade_price")
    private String candleAccTradePrice;
    @JsonProperty("candle_acc_trade_volume")
    private String candleAccTradeVolume;
    private String unit;
}
