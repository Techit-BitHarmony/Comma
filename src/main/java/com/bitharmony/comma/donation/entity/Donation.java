package com.bitharmony.comma.donation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bitharmony.comma.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Donation {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @NotNull
    private Member patron;

    @NotNull
    private String artistUsername;

    @NotNull
    private Integer amount;

    private String message;

    @Builder.Default
    private boolean anonymous = false;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

}

