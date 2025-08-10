package com.example.board.web.view;

import com.example.board.service.ArticleService;
import com.example.board.web.dto.ArticleCreateRequest;
import com.example.board.web.dto.ArticleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class ArticleViewController {
    private final ArticleService articleService;

    @GetMapping("/") public String index(){ return "index"; }

    @GetMapping({"/articles"})
    public String list(Model model){
        model.addAttribute("articles", articleService.findAll());
        return "article-list";
    }

    @GetMapping("/articles/new")
    public String createForm(){ return "article-form"; }

    @PostMapping("/articles")
    public String create(ArticleCreateRequest req){
        articleService.create(req);
        return "redirect:/articles";
    }

    @GetMapping("/articles/{id}")
    public String detail(@PathVariable Long id, Model model){
        var article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article-detail";
    }

//    @GetMapping("/articles/test-error")
//    public String testError() {
//        // 절대 존재하지 않을만한 ID
//        articleService.findById(9999L);
//        return "ok";
//    }

    @GetMapping("/articles/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        var article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, ArticleUpdateRequest req){
        articleService.update(id, req);
        return "redirect:/articles/" + id; // 수정 후 상세로 이동
    }

    @PostMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id){
        articleService.delete(id);
        return "redirect:/articles";
    }



}
