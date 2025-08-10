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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> create(@Valid @RequestBody ArticleCreateRequest req) {
        articleService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

