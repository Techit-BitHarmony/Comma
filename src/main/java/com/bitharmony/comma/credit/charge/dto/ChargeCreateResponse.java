package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;

@Builder
public record ChargeCreateResponse(
        long chargeId
) {
}
