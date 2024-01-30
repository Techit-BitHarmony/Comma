package com.bitharmony.comma.domain.credit.charge.dto;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import lombok.Builder;

@Builder
public record ChargeCreateRequest(
        long chargeAmount

) {
    public ChargeCreateRequest(Charge charge) {
        this(charge.getChargeAmount());
    }
}
