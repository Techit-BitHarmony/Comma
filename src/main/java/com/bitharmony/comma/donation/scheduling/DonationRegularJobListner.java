package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.service.DonationRegularService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DonationRegularJobListner implements JobListener {

    private final DonationRegularService donationRegularService;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void jobToBeExecuted(final JobExecutionContext context) {
        if (context.getJobDetail() == null) {
            log.info("start job");
        }
        JobKey key = context.getJobDetail().getKey();
        log.info("실행될 job의 jobkey = {}", key);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        JobKey key = context.getJobDetail().getKey();
        log.info("중단된 job의 jobkey = {}", key);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }

    private void updateSchedule(
            final JobExecutionContext context,
            final JobDataMap jobDataMap
    ) {
        log.info("새로운 job 업데이트");
    }
}
