package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;
import org.json.simple.JSONObject;

@Builder
public record ChargeConfirmResponse(
        JSONObject paymentStatement,
        boolean isApproved
) {
}
