package com.bitharmony.comma.donation.dto;

import lombok.*;

@Builder
public record DonationFindResponseDto(
        String patronUsername,
        Integer amount,
        String message
        ) {
}
