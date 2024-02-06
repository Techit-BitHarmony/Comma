package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationRegularRequestDto;
import com.bitharmony.comma.donation.dto.DonationRegularUpdateRequestDto;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.repository.DonationRegularRepository;
import com.bitharmony.comma.donation.scheduling.DonationRegularJobDetailService;
import com.bitharmony.comma.donation.scheduling.DonationRegularTriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.quartz.JobKey.jobKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationRegularService {
    private final Scheduler scheduler;
    private final DonationRegularTriggerService triggerService;
    private final DonationRegularJobDetailService jobDetailService;
    private final DonationRegularRepository donationRegularRepository;

    public void donationRegular(DonationRegularRequestDto dto) {

        JobKey jobKey = makeJobKey(dto.patronName(), dto.artistName());
        // check duplication


        DonationRegular donationRegular = saveDonationRegularEntity(dto, jobKey);
        JobDetail jobDetail = null;
        try {
            jobDetail = makeJobDetail(jobKey, donationRegular);
        }catch (IllegalAccessException e){

        }
        Trigger trigger = makeTrigger(jobKey, donationRegular);

        makeSchedule(jobDetail, trigger);
    }

    public void updateExecuteDay(DonationRegularUpdateRequestDto updateRequestDto) {
        JobKey jobKey = makeJobKey(updateRequestDto.patronName(), updateRequestDto.artistName());

        DonationRegular donationRegular = getDonationRegularByJobKey(jobKey);
        donationRegular.toBuilder().executeDay(updateRequestDto.executeDay()).build();
        donationRegularRepository.save(donationRegular);

        Trigger trigger = triggerService.update(scheduler, jobKey, updateRequestDto.executeDay());

        try {
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } catch (SchedulerException e) {

        }
    }

    private DonationRegular saveDonationRegularEntity(DonationRegularRequestDto dto, JobKey jobKey) {
        DonationRegular donationRegular = DonationRegular.builder()
                .patronName(dto.patronName())
                .artistName(dto.artistName())
                .amount(dto.amount())
                .executeDay(dto.executeDay())
                .jobKey(jobKey)
                .anonymous(dto.anonymous())
                .build();

        donationRegularRepository.save(donationRegular);

        return donationRegular;
    }

    private void makeSchedule(JobDetail jobDetail, Trigger trigger) {
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    private JobKey makeJobKey(String patronName, String artistName) {
        JobKey jobKey = jobKey(
                patronName
                        + "To"
                        + artistName
                , patronName);

        log.info("Created jobkey: ", jobKey.getName());
        return jobKey;
    }

    private JobDetail makeJobDetail(JobKey jobKey, DonationRegular donationRegular) throws IllegalAccessException {
        return jobDetailService.build(jobKey, donationRegular);
    }

    private Trigger makeTrigger(JobKey jobKey, DonationRegular donationRegular) {
        return triggerService.build(jobKey, donationRegular);
    }

    private DonationRegular getDonationRegularByJobKey(JobKey jobKey) {

        Optional<DonationRegular> donationRegularOp = donationRegularRepository.findByJobKey(jobKey);

        if (donationRegularOp.isPresent()) {
            return donationRegularOp.get();
        } else {
            throw new RuntimeException();
        }
    }
}
