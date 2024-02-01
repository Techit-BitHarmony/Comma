package com.bitharmony.comma.donation.service;

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

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();

        // 정기 후원 로직 작성

    }
}
