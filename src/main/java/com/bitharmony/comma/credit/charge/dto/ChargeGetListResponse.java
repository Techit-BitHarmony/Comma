package com.bitharmony.comma.credit.charge.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChargeGetListResponse(
        List<ChargeDto> chargeDtos
) {
}
