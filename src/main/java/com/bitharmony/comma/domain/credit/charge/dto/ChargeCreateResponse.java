package com.bitharmony.comma.domain.credit.charge.dto;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChargeCreateResponse(
        long chargeAmount,
        LocalDateTime createDate,
        String paymentKey,
        LocalDateTime payDate

) {
    public ChargeCreateResponse(Charge charge) {
        this(
                charge.getChargeAmount(),
                charge.getCreateDate(),
                charge.getPaymentKey(),
                charge.getPayDate());
    }
}
