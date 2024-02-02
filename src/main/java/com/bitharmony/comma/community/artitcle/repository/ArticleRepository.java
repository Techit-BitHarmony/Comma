package com.bitharmony.comma.community.artitcle.repository;

import com.bitharmony.comma.community.artitcle.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
