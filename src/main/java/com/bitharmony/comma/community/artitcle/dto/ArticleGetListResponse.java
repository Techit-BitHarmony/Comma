package com.bitharmony.comma.community.artitcle.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ArticleGetListResponse(
        List<ArticleDto> articleList
) {
}
