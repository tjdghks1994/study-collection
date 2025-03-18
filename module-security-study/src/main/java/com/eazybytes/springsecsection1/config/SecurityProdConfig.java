package com.eazybytes.springsecsection1.config;

import com.eazybytes.springsecsection1.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.springsecsection1.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class SecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(smc ->
                        // 세션이 존재하지 않으면 설정된 url 로 이동
                        // 동시 세션 제어를 위해 동시 세션은 최대 1개로만 설정
                        // 만약 이미 유효한 세션으로 인증을 요청하게 되면 인증에 실패하도록 설정
                        smc.invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc-> rcc.anyRequest().requiresSecure())   // https 만 허용
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated() // 보안
                        .requestMatchers("/notices", "/contact", "/error", "/register","/invalidSession").permitAll());  // 보안 해제
        http.formLogin(withDefaults());
        http.httpBasic(
                hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ech -> ech.accessDeniedHandler(new CustomAccessDeniedHandler()));
//        http.formLogin(AbstractHttpConfigurer::disable);  // formLogin 비활성화
//        http.httpBasic(AbstractHttpConfigurer::disable);  // httpBasic 비활성화
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // spring security 6.3 이후부터 도입
    // 사용자 비밀번호가 유출된 비밀번호인지 (강력한 비밀번호인지) 여부를 확인
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
