package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.domain.User;
import lombok.Getter;

@Getter
public class UserDto {
    private final String name;
    private final String gender;

    private UserDto(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public static UserDto of(User user) {
        return new UserDto(user.getName(), user.getGender());
    }
}
