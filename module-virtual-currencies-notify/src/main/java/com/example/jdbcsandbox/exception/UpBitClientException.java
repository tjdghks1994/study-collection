package com.example.jdbcsandbox.exception;

public class UpBitClientException extends RuntimeException {
    public UpBitClientException(String message) {
        super(String.format("UpBit 통신 중 오류 발생 = %s", message));
    }
}
