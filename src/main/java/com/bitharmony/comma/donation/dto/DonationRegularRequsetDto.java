package com.bitharmony.comma.donation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.joda.time.LocalDate;

@Builder
public record DonationRegularRequsetDto(
        @NotEmpty
        String patronName,

        @NotEmpty
        String artistName,

        @NotEmpty
        Integer amount,

        @NotEmpty
        Integer executeDay,

        boolean anonymous
) {
        public DonationRegularRequsetDto{
                executeDay = new LocalDate().getDayOfMonth();
                anonymous = false;
        }
}
