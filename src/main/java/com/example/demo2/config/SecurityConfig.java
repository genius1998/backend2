package com.example.demo2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청에 대해 인증을 필요로 하지 않음
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 로그인 페이지 경로
                        .defaultSuccessUrl("/loginSuccess") // 로그인 성공 시 리디렉션 경로
                        .failureUrl("/loginFailure") // 로그인 실패 시 리디렉션 경로
                )
                .csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화

        return http.build();
    }

    // CORS 설정을 추가하는 WebMvcConfigurer 빈을 추가
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 엔드포인트에 대해
                        .allowedOrigins("http://localhost:3000", "http://172.30.126.174:8080", "http://172.30.126.174:3000") // 프론트엔드 도메인 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 허용할 HTTP 메소드
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true); // 자격 증명 허용
            }
        };
    }
}
