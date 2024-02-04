package com.bitharmony.comma.community.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentCreateResponse(
        long articleId
) {

}
