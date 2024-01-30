package com.bitharmony.comma.domain.credit.withdraw.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    // 멤버 기능 추가시 수정 예정
//    @ManyToOne
//    private Member applicant;

    @NotNull
    private String bankName;

    @NotNull
    private String bankAccountNo;

    @NotNull
    private long withdrawAmount;

    private LocalDateTime applyDate;

    private LocalDateTime withdrawCancelDate;

    private LocalDateTime withdrawDoneDate;
}