package com.bitharmony.comma.community.comment.dto;

import com.bitharmony.comma.community.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentCreateRequest(
        @NotNull
        long articleId,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

}
