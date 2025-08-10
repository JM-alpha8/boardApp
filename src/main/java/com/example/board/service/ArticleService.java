package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.User;
import com.example.board.repository.ArticleRepository;
import com.example.board.repository.UserRepository;
import com.example.board.web.dto.ArticleCreateRequest;
import com.example.board.web.dto.ArticleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public List<Article> findAll(){ return articleRepository.findAll(); }

    @Transactional
    public Long create(ArticleCreateRequest req, String email){
        // ★ 없으면 즉시 생성(벨트+서스펜더)
        User author = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .name("OAuth2 User")
                        .password(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()))
                        .role("USER")
                        .build()));

        Article article = Article.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .author(author)
                .build();

        articleRepository.save(article);
        return article.getId();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found: " + id));
    }

    @PreAuthorize("@articleAuth.isOwner(#id, authentication) or hasRole('ADMIN')")
    @Transactional
    public void update(Long id, ArticleUpdateRequest req){
        var article = findById(id);
        article.update(req.getTitle(), req.getContent());
    }

    @PreAuthorize("@articleAuth.isOwner(#id, authentication) or hasRole('ADMIN')")
    @Transactional
    public void delete(Long id){
        articleRepository.deleteById(id);
    }

}
