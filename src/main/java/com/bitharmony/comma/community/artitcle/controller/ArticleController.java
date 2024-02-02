package com.bitharmony.comma.community.artitcle.controller;

import com.bitharmony.comma.community.artitcle.dto.ArticleCreateRequest;
import com.bitharmony.comma.community.artitcle.dto.ArticleCreateResponse;
import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.dto.ArticleGetResponse;
import com.bitharmony.comma.community.artitcle.service.ArticleService;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<ArticleGetResponse> getArticle(@PathVariable long id){

        Article article = articleService.getArticleById(id);

        return GlobalResponse.of(
                "200",
                "성공",
                ArticleGetResponse.builder()
                        .username(article.getWriter().getUsername())
                        .category(article.getCategory())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .createDate(article.getCreateDate())
                        .modifyDate(article.getModifyDate())
                        .build()
        );
    }



    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<ArticleCreateResponse> createArticle(
            @RequestBody ArticleCreateRequest request, Principal principal){
        Member member = memberService.getMemberByUsername(principal.getName());

        Article article = articleService.write(member, request.category(), request.title(), request.content());

        return GlobalResponse.of(
                "200",
                "글 작성 성공",
                ArticleCreateResponse.builder()
                        .id(article.getId())
                        .build()
        );
    }



}
