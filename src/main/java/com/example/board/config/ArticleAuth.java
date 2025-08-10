package com.example.board.config;

import com.example.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

// config/ArticleAuth.java
@Component
@RequiredArgsConstructor
public class ArticleAuth {
    private final ArticleRepository articleRepository;

    public boolean isOwner(Long articleId, Authentication authentication) {
        return articleRepository.findById(articleId)
                .map(a -> a.getAuthor().getEmail().equals(authentication.getName()))
                .orElse(false);
    }
}

