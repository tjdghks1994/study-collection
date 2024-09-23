package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.exception.UpBitClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UpBitClientException.class)
    public String handlerUpBitClientException(UpBitClientException e) {
        return e.getMessage();
    }
}
