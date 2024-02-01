package com.bitharmony.comma.domain.credit.charge.dto;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.creditLog.dto.CreditLogDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ChargeGetListResponse(
        List<ChargeDto> chargeDtos
) {
}
