package com.example.cryptoautomation.service;

import com.example.cryptoautomation.http.SlackHttpClient;
import com.example.cryptoautomation.http.UpBitHttpClient;
import com.example.cryptoautomation.http.UpBitTickerDto;
import com.example.cryptoautomation.repository.ReportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpBitSlackService {
    private final SlackHttpClient slackHttpClient;
    private final UpBitHttpClient upBitHttpClient;
    private final ReportHistoryRepository repository;
    public void execute(String market) {
        // upbit
        UpBitTickerDto tickerByMarket = upBitHttpClient.getTickerByMarket(market);

        // slack
        StringBuilder sb = new StringBuilder();
        sb.append("[실시간 데이터] ").append(market).append(" price = ").append(tickerByMarket.getTradePrice());
        slackHttpClient.send(sb.toString());

        repository.save(market, String.valueOf(tickerByMarket.getTradePrice()));
    }
}
