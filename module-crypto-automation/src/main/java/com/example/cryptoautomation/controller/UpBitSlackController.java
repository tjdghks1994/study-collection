package com.example.cryptoautomation.controller;

import com.example.cryptoautomation.service.UpBitSlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpBitSlackController {
    private final UpBitSlackService upBitSlackService;
    @GetMapping("/api/v1/ticker/{market}")
    public void callUpBit(@PathVariable String market) {
        upBitSlackService.execute(market);
    }
}
