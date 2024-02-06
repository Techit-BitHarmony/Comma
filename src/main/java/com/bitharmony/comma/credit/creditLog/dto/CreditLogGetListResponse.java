package com.bitharmony.comma.credit.creditLog.dto;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import lombok.Builder;

import java.util.List;


@Builder
public record CreditLogGetListResponse(
        List<CreditLogDto> creditLogDtos

        ) {
}
