package com.bitharmony.comma.streaming.dto;

import com.bitharmony.comma.streaming.entity.Status;
import com.bitharmony.comma.streaming.util.EncodeStatus;
import lombok.Builder;

@Builder
public record EncodeStatusResponse(
        EncodeStatus status
) {

    public static EncodeStatusResponse from(Status status) {
        return EncodeStatusResponse.builder()
                .status(status.getEncodeStatus())
                .build();
    }

}
