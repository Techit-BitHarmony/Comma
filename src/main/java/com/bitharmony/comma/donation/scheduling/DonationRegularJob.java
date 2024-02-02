package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@DisallowConcurrentExecution // 중복 실행 방지
@PersistJobDataAfterExecution
public class DonationRegularJob implements Job {
    private final DonationService donationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        DonationRegular dto = (DonationRegular) mergedJobDataMap.get("donationRegular");

        // 정기 후원 실행
        donationService.donateRegularToArtist(dto);

    }
}
