package com.bitharmony.comma.credit.withdraw.entity;

import com.bitharmony.comma.member.entity.Member;
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

    @ManyToOne
    private Member applicant;

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
