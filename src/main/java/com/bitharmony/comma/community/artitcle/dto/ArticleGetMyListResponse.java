package com.bitharmony.comma.community.artitcle.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArticleGetMyListResponse(
        List<ArticleDto> myList
) {
}
