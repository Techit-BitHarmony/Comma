package com.bitharmony.comma.credit.withdraw.dto;

import lombok.Builder;

@Builder
public record WithdrawModifyRequest(
        String bankName,
        String bankAccountNo,
        long withdrawAmount
) {

}
