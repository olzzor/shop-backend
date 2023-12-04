package com.bridgeshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(form -> form.disable()) // FormLogin 비활성화
                .httpBasic(http -> http.disable()) // httpBasic 비활성화
                .csrf(csrf -> csrf.disable()) // CSRF(Cross-Site Request Forgery) 방어 기능 비활성화
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(auth -> auth
                        .requestMatchers("/",
                                "/error",
                                "/favicon.ico",
                                "/**",
                                "/login/**").permitAll() // 모든 사용자 접근 가능
                        .anyRequest().authenticated()); // 위의 경로 이외에는 인증된 사용자만 접근 가능
        return httpSecurity.build();
    }
}