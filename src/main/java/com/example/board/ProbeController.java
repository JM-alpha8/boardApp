package com.example.board;

import com.example.board.domain.Article;
import com.example.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProbeController {
    private final ArticleRepository repo;

    @GetMapping("/probe/count")
    public long count() {
        return repo.count();
    }

    @GetMapping("/probe/list")
    public List<Article> list() {
        return repo.findAll();
    }
}
