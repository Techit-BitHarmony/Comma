package com.bitharmony.comma.credit.charge.dto;

import com.bitharmony.comma.credit.charge.entity.Charge;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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
