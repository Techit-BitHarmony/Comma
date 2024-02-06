package com.bitharmony.comma.community.comment.dto;

import com.bitharmony.comma.community.artitcle.entity.Article;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentModifyRequest(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
}
