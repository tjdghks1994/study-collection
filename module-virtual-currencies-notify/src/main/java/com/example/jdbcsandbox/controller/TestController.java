package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/test")
    public String helloWorld() {
        return service.getTest();
    }
}
