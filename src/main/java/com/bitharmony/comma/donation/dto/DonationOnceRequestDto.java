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
        Long amount,

        String message,

        boolean anonymous
) {}
