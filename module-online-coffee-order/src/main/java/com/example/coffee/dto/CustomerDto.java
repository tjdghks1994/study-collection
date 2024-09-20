package com.example.coffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDto {
    private final String name;
    private final String address;
    private final String phoneNumber;
}
