package com.bitharmony.comma.community.artitcle.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import lombok.Builder;

@Builder
public record ArticleModifyResponse(
        long id
) {
}
