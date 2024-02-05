package com.bitharmony.comma.credit.withdraw.dto;

import lombok.Builder;

@Builder
public record WithdrawCancelResponse(
        long id,
        String resultMsg
) {

}
