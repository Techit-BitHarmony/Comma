package com.bitharmony.comma.community.artitcle.service;

import com.bitharmony.comma.community.artitcle.dto.ArticleModifyRequest;
import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.repository.ArticleRepository;
import com.bitharmony.comma.global.exception.ArticleNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article getArticleById(long id) {
        Optional<Article> article = articleRepository.findById(id);

        if(article.isEmpty()){
            throw new ArticleNotFoundException();
        }

        return article.get();
    }

    public Article write(Member writer, Article.Category category, String title, String content) {
        Article article = Article.builder()
                .writer(writer)
                .category(category)
                .title(title)
                .content(content)
                .build();

        articleRepository.save(article);

        return article;
    }

    public List<Article> getArticleByMemberId(Long id) {
        return articleRepository.findByWriterId(id);
    }

    public void modifyArticle(Article article, ArticleModifyRequest request) {
        Article _article = article.toBuilder()
                .category(request.category())
                .title(request.title())
                .content(request.content())
                .modifyDate(LocalDateTime.now())
                .build();

        articleRepository.save(_article);
    }

    public void deleteArticle(long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> getArticleList() {
        return articleRepository.findAll();
    }
}
