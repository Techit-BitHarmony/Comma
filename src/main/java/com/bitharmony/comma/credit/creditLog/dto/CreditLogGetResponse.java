package com.bitharmony.comma.credit.creditLog.dto;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreditLogGetResponse(
        CreditLog.EventType eventType,
        long creditChangeAmount,
        LocalDateTime createDate

        ) {
    public CreditLogGetResponse(CreditLog creditLog){
        this(
                creditLog.getEventType(),
                creditLog.getCreditChangeAmount(),
                creditLog.getCreateDate());
    }
}
