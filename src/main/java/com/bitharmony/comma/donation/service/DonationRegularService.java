package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationRegularService {
    private final DonationRepository donationRepository;

}
