package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChargeGetResponse(
        long chargeAmount,
        String username,
        String chargeCode,
        LocalDateTime createDate,
        String paymentKey,
        LocalDateTime payDate

) {
}
