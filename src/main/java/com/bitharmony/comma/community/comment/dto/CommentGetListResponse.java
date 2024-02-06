package com.bitharmony.comma.community.comment.dto;

import com.bitharmony.comma.community.artitcle.dto.ArticleDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentGetListResponse(
        List<CommentDto> commentList
) {
}
