package com.bitharmony.comma.credit.creditLog.dto;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.member.entity.Member;
import lombok.Builder;

import java.util.List;


@Builder
public record CreditLogGetListResponse(
        long restCredit,
        List<CreditLogDto> creditLogDtos

        ) {
}
