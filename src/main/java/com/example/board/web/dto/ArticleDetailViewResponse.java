package com.example.board.web.dto;

import com.example.board.domain.Article;
import lombok.Getter;

@Getter
public class ArticleDetailViewResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String authorEmail;
    public ArticleDetailViewResponse(Article a){
        this.id = a.getId();
        this.title = a.getTitle();
        this.content = a.getContent();
        this.authorEmail = a.getAuthor() != null ? a.getAuthor().getEmail() : null;
    }
}

