package com.bitharmony.comma.community.artitcle.repository;

import com.bitharmony.comma.community.artitcle.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByWriterId(Long id);
}
