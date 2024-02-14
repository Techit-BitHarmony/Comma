package com.bitharmony.comma.community.artitcle.repository;

import com.bitharmony.comma.community.artitcle.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByWriterId(Long id);

    Page<Article> findAll(Pageable pageable);

    Page<Article> findByArtistId(long id, Pageable pageable);
}
