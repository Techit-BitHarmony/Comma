package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.scheduling.DonationRegularJobDetailService;
import com.bitharmony.comma.donation.scheduling.DonationRegularTriggerService;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DonationRegularService {
    private final Scheduler scheduler;
    private final DonationRegularTriggerService trigger;
    private final DonationRegularJobDetailService jobDetail;



}
