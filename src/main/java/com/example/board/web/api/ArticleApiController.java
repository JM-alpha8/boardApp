package com.example.board.web.api;

import com.example.board.domain.Article;
import com.example.board.service.ArticleService;
import com.example.board.web.dto.ArticleCreateRequest;
import com.example.board.web.dto.ArticleUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;


import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ArticleCreateRequest req,
                                       Principal principal,
                                       @AuthenticationPrincipal OAuth2User oauth2User,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("[API] principal=" + (principal!=null?principal.getName():"null")
                + ", oauth2.email=" + (oauth2User!=null?oauth2User.getAttribute("email"):"null")
                + ", userDetails=" + (userDetails!=null?userDetails.getUsername():"null"));

        String email = null;

        // 1) 구글 OAuth2 로그인
        if (oauth2User != null && oauth2User.getAttribute("email") != null) {
            email = oauth2User.getAttribute("email");
        }
        // 2) 폼 로그인
        else if (userDetails != null) {
            email = userDetails.getUsername(); // username=이메일
        }
        // 3) 마지막 보루 (가능하면 쓰지 않기)
        else if (principal != null) {
            email = principal.getName(); // 구글이면 sub일 수 있음
        }

        articleService.create(req, email);
        return ResponseEntity.created(URI.create("/articles")).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest req) {
        articleService.update(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

