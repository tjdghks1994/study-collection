package com.fastcampus.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    public WebConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                            JwtExceptionFilter jwtExceptionFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtExceptionFilter = jwtExceptionFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000")); // 허용할 출처
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE")); // 허용할 HTTP 메소드
        corsConfiguration.setAllowedHeaders(List.of("*"));                          // 모든 헤더 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", corsConfiguration);      // 특정 url 에만 적용

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users",
                                "/api/v1/users/authenticate")
                        .permitAll()    // 위 requestMatchers 패턴에 해당하는 url 은 인증 해제
                        .anyRequest()   // 그 외 모든 요청에 인증 처리
                        .authenticated())
                // rest api 이므로 세션 비활성화
                .sessionManagement(
                        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)          // csrf 비활성화
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   // JWT 검증 필터 추가
                .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())    // JWT 예외 필터 추가
                .httpBasic(Customizer.withDefaults());  // basic auth 사용

        return httpSecurity.build();
    }
}
