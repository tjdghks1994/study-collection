package com.example.coffee.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<T> {

    private final HttpStatus statusCode;
    private final String message;
    private final T data;

    public Response(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(HttpStatus.OK, "SUCCESS", data);
    }

    public static <T> Response<T> fail(String errorMessage) {
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, null);
    }
}
