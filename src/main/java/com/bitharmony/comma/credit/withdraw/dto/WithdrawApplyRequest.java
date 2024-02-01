package com.bitharmony.comma.credit.withdraw.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WithdrawApplyRequest(
        String bankName,
        String bankAcountNo,
        long withdrawAmount
) {

}
