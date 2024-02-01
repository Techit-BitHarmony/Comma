package com.bitharmony.comma.credit.withdraw.dto;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WithdrawGetResponse(
        long id,
        String bankName,
        String bankAcountNo,
        long withdrawAmount,
        LocalDateTime applyDate,
        LocalDateTime withdrawCancelDate,
        LocalDateTime withdrawDoneDate

) {
    public WithdrawGetResponse(Withdraw withdraw){
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
