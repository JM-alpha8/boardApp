package com.example.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {

    @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/articles", "/articles/**", "/login", "/join", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/**").authenticated() // CUD 보호
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")        // 커스텀 로그인 페이지
                        .loginProcessingUrl("/login") // POST 처리 URL (기본)
                        .defaultSuccessUrl("/articles", true)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")                  // 같은 로그인 페이지 사용
                        .defaultSuccessUrl("/articles", true) // 로그인 성공 후 이동
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/articles")
                )
                // 개발 편의: API는 CSRF 예외. (운영에선 토큰 방식 권장)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        ;
        return http.build();
    }
}

