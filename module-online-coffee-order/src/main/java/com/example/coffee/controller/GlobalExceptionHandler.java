package com.example.coffee.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public Response<Void> handleRuntimeException(RuntimeException e) {
        return Response.fail(e.getMessage());
    }
}
