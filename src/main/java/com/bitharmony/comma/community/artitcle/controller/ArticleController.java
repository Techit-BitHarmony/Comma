package com.bitharmony.comma.community.artitcle.controller;

import com.bitharmony.comma.community.artitcle.dto.*;
import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.service.ArticleService;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<ArticleGetMyListResponse> getMyArticle(Principal principal){
        Member member = memberService.getMemberByUsername(principal.getName());

        List<Article> articles = articleService.getArticleByMemberId(member.getId());

        return GlobalResponse.of(
                "200",
                "글 불러오기 성공",
                ArticleGetMyListResponse.builder()
                        .myList(articles.stream()
                                .map(ArticleDto::new)
                                .toList())
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

    @PutMapping("/{id}")
    public GlobalResponse<ArticleModifyResponse> modifyArticle(
            @PathVariable long id, Principal principal, @RequestBody ArticleModifyRequest request){

        Member member = memberService.getMemberByUsername(principal.getName());
        Article article = articleService.getArticleById(id);

        if(!article.getWriter().equals(member)){
            throw new NotAuthorizedException();
        }

        articleService.modifyArticle(article, request);

        return GlobalResponse.of(
                "200",
                "글 수정 성공",
                ArticleModifyResponse.builder()
                        .id(article.getId())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public GlobalResponse<Void> deleteAriticle(@PathVariable long id, Principal principal){

        Member member = memberService.getMemberByUsername(principal.getName());
        Article article = articleService.getArticleById(id);

        if(!article.getWriter().equals(member)){
            throw new NotAuthorizedException();
        }

        articleService.deleteArticle(id);

        return GlobalResponse.of(
                "204",
                "글 삭제 성공"
        );
    }



}
