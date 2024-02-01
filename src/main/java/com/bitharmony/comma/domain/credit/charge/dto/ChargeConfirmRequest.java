package com.bitharmony.comma.domain.credit.charge.dto;

import lombok.Builder;

@Builder
public record ChargeConfirmRequest(
        String paymentKey,
        String orderId,
        String amount

        ) {
}
