package com.bitharmony.comma.community.comment.dto;

import lombok.Builder;

@Builder
public record CommentModifyResponse(
        long articleId
) {
}
