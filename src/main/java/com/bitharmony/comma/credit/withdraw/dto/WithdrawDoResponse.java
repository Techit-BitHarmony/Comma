package com.bitharmony.comma.credit.withdraw.dto;

import lombok.Builder;

@Builder
public record WithdrawDoResponse(
        long id,
        String resultMsg
) {

}
