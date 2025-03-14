package com.eazybytes.springsecsection1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated() // 보안
                        .requestMatchers("/notices", "/contact", "/error", "/register").permitAll());  // 보안 해제
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
//        http.formLogin(AbstractHttpConfigurer::disable);  // formLogin 비활성화
//        http.httpBasic(AbstractHttpConfigurer::disable);  // httpBasic 비활성화
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("user").password("{bcrypt}$2a$12$z0LNVUbjpa6dUSbpGeeaf.WTRLGRXk7bRbdxiKX3b8A9vzGPRizUi").authorities("read").build();
//        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$z0LNVUbjpa6dUSbpGeeaf.WTRLGRXk7bRbdxiKX3b8A9vzGPRizUi").authorities("admin").build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

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
