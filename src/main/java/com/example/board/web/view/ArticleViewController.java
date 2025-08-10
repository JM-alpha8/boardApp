package com.example.board.web.view;

import com.example.board.domain.Article;
import com.example.board.service.ArticleService;
import com.example.board.web.dto.ArticleCreateRequest;
import com.example.board.web.dto.ArticleDetailViewResponse;
import com.example.board.web.dto.ArticleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

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
    public String create(ArticleCreateRequest req,
                         Principal principal,
                         @AuthenticationPrincipal OAuth2User oauth2User,
                         @AuthenticationPrincipal UserDetails userDetails) {

        String email = null;

        // 1) 구글 OAuth2 로그인일 때
        if (oauth2User != null && oauth2User.getAttribute("email") != null) {
            email = oauth2User.getAttribute("email");
        }
        // 2) 폼 로그인일 때
        else if (userDetails != null) {
            email = userDetails.getUsername(); // = email로 세팅되어 있음
        }
        // 3) 마지막 보루
        else if (principal != null) {
            email = principal.getName(); // (여전히 sub일 수 있음)
        }

        articleService.create(req, email);
        return "redirect:/articles";
    }

    @GetMapping("/articles/{id}")
    public String detail(@PathVariable Long id, Model model){
        Article a = articleService.findById(id); // 필요하면 fetch join
        model.addAttribute("article", new ArticleDetailViewResponse(a));
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
