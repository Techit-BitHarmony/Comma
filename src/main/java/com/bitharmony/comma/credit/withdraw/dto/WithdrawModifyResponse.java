package com.bitharmony.comma.credit.withdraw.dto;

import lombok.Builder;

@Builder
public record WithdrawModifyResponse(
        String bankName,
        String bankAccountNo,
        long withdrawAmount
) {

}
