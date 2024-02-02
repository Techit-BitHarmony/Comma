package com.bitharmony.comma.community.artitcle.controller;

import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.entity.ArticleGetResponse;
import com.bitharmony.comma.community.artitcle.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleGetResponse> getArticle(@PathVariable long id){

        Article article = articleService.getArticleById(id);

        return new ResponseEntity<>(
                ArticleGetResponse.builder()
                        .writer(article.getWriter())
                        .category(article.getCategory())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .createDate(article.getCreateDate())
                        .modifyDate(article.getModifyDate())
                        .build(),
                HttpStatus.OK
        );
    }



}
