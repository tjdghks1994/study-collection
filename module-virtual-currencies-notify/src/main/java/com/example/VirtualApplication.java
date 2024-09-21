package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:/jdbc.properties"),
        @PropertySource("classpath:/jdbc-local.properties")
})
public class VirtualApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirtualApplication.class, args);
    }
}
