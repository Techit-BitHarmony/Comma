package com.bitharmony.comma.community.artitcle.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import lombok.Builder;

@Builder
public record ArticleModifyRequest(
        Article.Category category,
        String title,
        String content
) {
}
