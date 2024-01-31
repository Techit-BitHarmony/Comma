package com.bitharmony.comma.donation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
public record DonationFindResponseDto(
        String patronUsername,
        String artistUsername,
        Integer amount,
        String message,
        LocalDateTime time
        ) {
}
