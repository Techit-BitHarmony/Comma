package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;

@Builder
public record ChargeConfirmRequest(
        String paymentKey,
        String orderId,
        String amount

        ) {
}
