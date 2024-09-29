package com.example.jdbcsandbox.controller;

import com.example.jdbcsandbox.service.TestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {

    @LocalServerPort
    int port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    TestController testController;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TestService testService;

    @Test
    void test() {
        // then
        assertThat(testController).isNotNull();
    }

    @Test
    void test2() {
        // when
        Mockito.when(testService.getTest()).thenReturn("Hello, Mock");
        String result = restTemplate
                .getForObject("http://localhost:" + port + "/api/v1/test", String.class);
        // then
        assertThat(result).contains("Hello, Mock");
    }

    @Test
    void mockBeanTest() throws Exception {
        Mockito.when(testService.getTest()).thenReturn("Hello, Mock");

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/test")
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello, Mock"));
    }
}