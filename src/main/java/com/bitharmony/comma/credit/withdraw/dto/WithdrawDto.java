package com.bitharmony.comma.credit.withdraw.dto;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WithdrawDto(
        long id,
        String applicantName,
        String bankName,
        String bankAccountNo,
        long withdrawAmount,
        LocalDateTime applyDate,
        LocalDateTime withdrawCancelDate,
        LocalDateTime withdrawDoneDate
) {
    public WithdrawDto(Withdraw withdraw){
        this(
                withdraw.getId(),
                withdraw.getApplicant().getUsername(),
                withdraw.getBankName(),
                withdraw.getBankAccountNo(),
                withdraw.getWithdrawAmount(),
                withdraw.getApplyDate(),
                withdraw.getWithdrawCancelDate(),
                withdraw.getWithdrawDoneDate()
        );
    }
}
