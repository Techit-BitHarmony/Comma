package com.bitharmony.comma.domain.credit.charge.dto;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChargeGetResponse(
        long chargeAmount,
        LocalDateTime createDate,
        String paymentKey,
        LocalDateTime payDate

) {
    public ChargeGetResponse(Charge charge) {
        this(
                charge.getChargeAmount(),
                charge.getCreateDate(),
                charge.getPaymentKey(),
                charge.getPayDate());
    }
}
