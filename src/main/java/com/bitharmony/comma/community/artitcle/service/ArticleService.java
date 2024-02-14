package com.bitharmony.comma.community.artitcle.service;

import com.bitharmony.comma.community.artitcle.dto.ArticleModifyRequest;
import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.repository.ArticleRepository;
import com.bitharmony.comma.global.exception.community.ArticleNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Article write(Member writer, Article.Category category, String title, String content, Member artist) {
        Article article = Article.builder()
                .writer(writer)
                .artist(artist)
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

    @Transactional
    public void deleteArticle(long id) {
        articleRepository.deleteById(id);
    }

    public Page<Article> getArticleList(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Page<Article> getArticleListByArtistIdAndCategory(long id, Article.Category category,Pageable pageable) {
        if (category == null) {
            return articleRepository.findByArtistId(id, pageable);
        } else {
            return articleRepository.findByArtistIdAndCategory(id, category, pageable);
        }
    }
}
