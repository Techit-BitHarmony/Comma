package com.bitharmony.comma.domain.credit.withdraw.dto;

import com.bitharmony.comma.domain.credit.withdraw.entity.Withdraw;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WithdrawDto(
        long id,
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
                withdraw.getBankName(),
                withdraw.getBankAccountNo(),
                withdraw.getWithdrawAmount(),
                withdraw.getApplyDate(),
                withdraw.getWithdrawCancelDate(),
                withdraw.getWithdrawDoneDate()
        );
    }
}
