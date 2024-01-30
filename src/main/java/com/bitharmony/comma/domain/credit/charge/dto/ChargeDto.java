package com.bitharmony.comma.domain.credit.charge.dto;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;

import java.time.LocalDateTime;

public record ChargeDto(
        long chargeAmount,
        LocalDateTime createDate,
        String paymentKey,
        LocalDateTime payDate
) {
    public ChargeDto(Charge charge){
        this(
                charge.getChargeAmount(),
                charge.getCreateDate(),
                charge.getPaymentKey(),
                charge.getPayDate());
    }
}
