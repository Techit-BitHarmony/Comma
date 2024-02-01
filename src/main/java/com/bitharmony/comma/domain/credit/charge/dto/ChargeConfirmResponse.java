package com.bitharmony.comma.domain.credit.charge.dto;

import lombok.Builder;
import org.json.simple.JSONObject;

@Builder
public record ChargeConfirmResponse(
        JSONObject paymentStatement,
        boolean isApproved
) {
}
