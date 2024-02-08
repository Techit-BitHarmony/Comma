package com.bitharmony.comma.community.artitcle.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record ArticleGetListResponse(
        Page<ArticleDto> articleList
) {
}
