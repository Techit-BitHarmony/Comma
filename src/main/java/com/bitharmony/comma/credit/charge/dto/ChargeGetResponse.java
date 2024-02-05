package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChargeGetResponse(
        long chargeAmount,
        String username,
        LocalDateTime createDate,
        String paymentKey,
        LocalDateTime payDate

) {
}
