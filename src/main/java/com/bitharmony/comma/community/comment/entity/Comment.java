package com.bitharmony.comma.community.comment.entity;

import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member commenter;

    @ManyToOne
    private Article article;

    @NotNull
    private String content;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime modifyDate;
}
