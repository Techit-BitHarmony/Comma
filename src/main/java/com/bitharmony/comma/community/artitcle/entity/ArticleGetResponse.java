package com.bitharmony.comma.community.artitcle.entity;

import com.bitharmony.comma.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleGetResponse(
        Member writer,
        Article.Category category,
        String title,
        String content,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
}
