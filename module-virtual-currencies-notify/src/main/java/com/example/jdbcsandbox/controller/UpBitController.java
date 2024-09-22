package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.service.MinuteCandle;
import com.example.jdbcsandbox.service.UpBitService;
import com.example.jdbcsandbox.validation.MinuteCandleValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UpBitController {
    private final MinuteCandleValidator minuteCandleValidator;
    private final UpBitService upBitService;

    public UpBitController(MinuteCandleValidator minuteCandleValidator, UpBitService upBitService) {
        this.minuteCandleValidator = minuteCandleValidator;
        this.upBitService = upBitService;
    }

    @GetMapping("/api/v1/candle/minute/{unit}")
    public List<MinuteCandle> getMinuteCandles(
            @PathVariable int unit,
            @RequestBody MinuteCandleRequest request,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        minuteCandleValidator.validate(unit, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ArrayList<>();
        }

        return upBitService.getCandles(unit, request);
    }
}
