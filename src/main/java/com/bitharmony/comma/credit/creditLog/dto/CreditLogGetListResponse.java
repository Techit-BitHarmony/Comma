package com.bitharmony.comma.credit.creditLog.dto;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.member.entity.Member;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;


@Builder
public record CreditLogGetListResponse(
        long restCredit,
        Page<CreditLogDto> creditLogDtos

        ) {
}
