package com.eazybytes.springsecsection1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated() // 보안
                .requestMatchers("/notices", "/contact", "/error").permitAll());  // 보안 해제
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);  // formLogin 비활성화
//        http.httpBasic(AbstractHttpConfigurer::disable);  // httpBasic 비활성화
        return http.build();
    }

}
