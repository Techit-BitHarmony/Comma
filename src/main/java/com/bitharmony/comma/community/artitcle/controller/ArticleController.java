package com.bitharmony.comma.community.artitcle.controller;

import com.bitharmony.comma.community.artitcle.dto.*;
import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.service.ArticleService;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
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
                ArticleGetResponse.builder()
                        .id(article.getId())
                        .username(article.getWriter().getUsername())
                        .category(article.getCategory())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .createDate(article.getCreateDate())
                        .modifyDate(article.getModifyDate())
                        .build()
        );
    }

    @GetMapping("")
    public GlobalResponse<ArticleGetListResponse> getArticleList(
            @RequestParam(value="page", defaultValue = "1") int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Article> articles = articleService.getArticleList(pageable);

        return GlobalResponse.of(
                "200",
                ArticleGetListResponse.builder()
                        .articleList(articles.map(ArticleDto::new))
                        .build()
        );
    }

    @GetMapping("/user/{artistUsername}")
    public GlobalResponse<ArticleGetListResponse> getArticleListByArtistIdAndCategory(
            @RequestParam(value="page", defaultValue = "1") int page,
            @RequestParam(value="category", defaultValue = "") Article.Category category,
            @PathVariable String artistUsername
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Member member = memberService.getMemberByUsername(artistUsername);

        Page<Article> articles = articleService.getArticleListByArtistIdAndCategory(member.getId(), category, pageable);

        return GlobalResponse.of(
                "200",
                ArticleGetListResponse.builder()
                        .articleList(articles.map(ArticleDto::new))
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
                ArticleGetMyListResponse.builder()
                        .myList(articles.stream().map(ArticleDto::new).toList())
                        .build()
                );
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<ArticleCreateResponse> createArticle(
            @RequestBody @Valid ArticleCreateRequest request, Principal principal){
        Member member = memberService.getMemberByUsername(principal.getName());
        Member artist = memberService.getMemberByUsername(request.artistUsername());

        Article article = articleService.write(member, request.category(), request.title(), request.content(), artist);

        return GlobalResponse.of(
                "200",
                ArticleCreateResponse.builder()
                        .id(article.getId())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<ArticleModifyResponse> modifyArticle(
            @PathVariable long id, Principal principal, @RequestBody @Valid ArticleModifyRequest request){

        Member member = memberService.getMemberByUsername(principal.getName());
        Article article = articleService.getArticleById(id);

        if(!article.getWriter().equals(member)){
            throw new NotAuthorizedException();
        }

        articleService.modifyArticle(article, request);

        return GlobalResponse.of(
                "200",
                ArticleModifyResponse.builder()
                        .id(article.getId())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<Void> deleteArticle(@PathVariable long id, Principal principal){

        Member member = memberService.getMemberByUsername(principal.getName());
        Article article = articleService.getArticleById(id);

        if(!article.getWriter().equals(member)){
            throw new NotAuthorizedException();
        }

        articleService.deleteArticle(id);

        return GlobalResponse.of(
                "204"
        );
    }

}
