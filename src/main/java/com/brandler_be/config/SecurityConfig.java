package com.brandler_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 * 개발 환경에서는 모든 요청을 허용합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Security Filter Chain 설정
     * 개발 환경에서는 모든 인증을 비활성화합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 개발 환경에서는 모든 보안 제약 사항 비활성화
        return http
            // CSRF 보호 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            // 모든 요청 허용
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            // X-Frame-Options 헤더 비활성화 (H2 콘솔 등을 위해)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            // 기본 HTTP 인증 비활성화
            .httpBasic(AbstractHttpConfigurer::disable)
            // 폼 로그인 비활성화
            .formLogin(AbstractHttpConfigurer::disable)
            .build();
    }
}
