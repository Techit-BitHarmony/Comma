package com.bitharmony.comma.donation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;

@Builder
public record DonationRequestDto(
        @NotEmpty
        String patronName,

        @NotEmpty
        String artistName,

        @NotEmpty
        Integer amount,

        String message,

        boolean anonymous
) {
    public DonationRequestDto{
        anonymous = false;
    }
}
