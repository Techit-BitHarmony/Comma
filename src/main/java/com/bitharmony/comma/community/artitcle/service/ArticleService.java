package com.bitharmony.comma.community.artitcle.service;

import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.repository.ArticleRepository;
import com.bitharmony.comma.global.exception.ArticleNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

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
}
