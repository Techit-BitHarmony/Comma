package com.bitharmony.comma.domain.credit.creditLog.dto;

import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;


@Builder
public record CreditLogGetListResponse(
        List<CreditLogDto> creditLogDtos

        ) {

    public static CreditLogGetListResponse toDtoList(List<CreditLog> creditLogs){
        List<CreditLogDto> creditLogDtos = creditLogs.stream()
                .map(CreditLogDto::new)
                .toList();

        return new CreditLogGetListResponse(creditLogDtos);
    }
}
