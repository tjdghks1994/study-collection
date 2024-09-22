package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/users")
    public UserDto create(@RequestParam String name, @RequestParam String gender) {
        return userService.create(name, gender);
    }

    @GetMapping("/api/v1/users/gender/{gender}")
    public List<UserDto> findByGender(@PathVariable String gender) {
        return userService.findByGender(gender);
    }

    @PutMapping("/api/v1/users/{id}")
    public UserDto updateName(@PathVariable Integer id, @RequestParam String name) {
        return userService.updateName(id, name);
    }

    @DeleteMapping("/api/v1/users/{id}")
    public UserDto delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}
