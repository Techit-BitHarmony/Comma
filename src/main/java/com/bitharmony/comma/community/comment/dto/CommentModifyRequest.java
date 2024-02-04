package com.bitharmony.comma.community.comment.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import lombok.Builder;

@Builder
public record CommentModifyRequest(
        String content
) {
}
