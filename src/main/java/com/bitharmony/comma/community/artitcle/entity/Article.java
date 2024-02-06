package com.bitharmony.comma.community.artitcle.entity;

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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Member writer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime modifyDate;

    public enum Category {
        공지사항,
        홍보,
        소통

    }

}
