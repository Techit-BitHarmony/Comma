package com.bitharmony.comma.donation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
public record DonationOnceRequestDto(
        @NotEmpty
        String patronName,

        @NotEmpty
        String artistName,

        @NotEmpty
        Integer amount,

        String message,

        boolean anonymous
) {
    public DonationOnceRequestDto {
        anonymous = false;
    }
}
