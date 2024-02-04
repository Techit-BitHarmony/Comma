package com.bitharmony.comma.community.comment.dto;

import com.bitharmony.comma.community.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentCreateRequest(
        long articleId,
        String username,
        String content
) {

}
