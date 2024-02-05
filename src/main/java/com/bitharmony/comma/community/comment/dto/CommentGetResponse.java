package com.bitharmony.comma.community.comment.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CommentGetResponse(
        String content

        ) {
}
