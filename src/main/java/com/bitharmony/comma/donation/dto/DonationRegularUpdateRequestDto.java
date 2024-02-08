package com.bitharmony.comma.donation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DonationRegularUpdateRequestDto(
        @NotNull
        String patronName,
        @NotNull
        String artistName,

        Integer executeDay
) {
}
