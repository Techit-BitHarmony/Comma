package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationRegularRequsetDto;
import com.bitharmony.comma.donation.scheduling.DonationRegularJobDetailService;
import com.bitharmony.comma.donation.scheduling.DonationRegularTriggerService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import static org.quartz.JobKey.jobKey;


@Service
@RequiredArgsConstructor
public class DonationRegularService {
    private final Scheduler scheduler;
    private final DonationRegularTriggerService triggerService;
    private final DonationRegularJobDetailService jobDetailService;

    public void makeSchedule(DonationRegularRequsetDto dto) throws SchedulerException {

        JobKey jobkey = jobKey(dto.patronName() + "to" + dto.artistName(), dto.patronName());

        JobDetail jobDetail = jobDetailService.build(jobkey, dto);

        Trigger trigger = triggerService.build(jobkey, dto);

        //exception 처리 필요
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
