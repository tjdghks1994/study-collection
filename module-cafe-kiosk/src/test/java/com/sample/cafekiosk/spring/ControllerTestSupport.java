package com.sample.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.cafekiosk.spring.api.controller.order.OrderController;
import com.sample.cafekiosk.spring.api.controller.product.ProductController;
import com.sample.cafekiosk.spring.api.service.order.OrderService;
import com.sample.cafekiosk.spring.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected OrderService orderService;
    @MockBean
    protected ProductService productService;
}
