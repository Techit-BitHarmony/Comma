package com.bitharmony.comma.community.artitcle.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleDto(
        String username,
        Article.Category category,
        String title,
        String content,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public ArticleDto(Article article){
        this(
                article.getWriter().getUsername(),
                article.getCategory(),
                article.getTitle(),
                article.getContent(),
                article.getCreateDate(),
                article.getModifyDate()
        );
    }

}
