package com.sample.cafekiosk.spring.api.controller.order;

import com.sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.sample.cafekiosk.spring.api.service.order.OrderService;
import com.sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        return orderService.createOrder(request, registeredDateTime);
    }
}
