package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.domain.Market;
import com.example.jdbcsandbox.repository.MarketRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {
    private final MarketRepository marketRepository;

    public MarketController(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    @GetMapping("/api/v1/querydsl/{market}")
    public Market getMarketByQuerydsl(@PathVariable String market) {
        return marketRepository.queryDsl(market);
    }

    @GetMapping("/api/v1/jpa/{market}")
    public Market getMarketByJpa(@PathVariable String market) {
        return marketRepository.jpa(market);
    }
}
