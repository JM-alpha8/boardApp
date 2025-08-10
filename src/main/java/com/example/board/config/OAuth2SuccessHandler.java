package com.example.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import com.example.board.repository.UserRepository;
import com.example.board.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // ← 로컬 사용

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder localEncoder = new BCryptPasswordEncoder(); // ← 빈 아님

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String name  = (String) oAuth2User.getAttributes().getOrDefault("name", "OAuth2 User");

        userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .name(name)
                        .password(localEncoder.encode(UUID.randomUUID().toString())) // 임시 해시
                        .role("USER")
                        .build())
        );

        res.sendRedirect("/articles");
    }
}


