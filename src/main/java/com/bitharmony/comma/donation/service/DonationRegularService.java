package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationRegularFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationRegularRequestDto;
import com.bitharmony.comma.donation.dto.DonationRegularUpdateRequestDto;
import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.repository.DonationRegularRepository;
import com.bitharmony.comma.donation.scheduling.DonationRegularJobDetailService;
import com.bitharmony.comma.donation.scheduling.DonationRegularTriggerService;
import com.bitharmony.comma.global.exception.Donation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public List<DonationRegularFindResponseDto> getAllDonationRegularList(String patronName){

        List<DonationRegular> donationRegulars = donationRegularRepository.findAllByPatronName(patronName);
        List<DonationRegularFindResponseDto> dtoList = new ArrayList<>();
        if (donationRegulars.size() == 0) {
            throw new DonationListNotFoundException();
        }

        for (DonationRegular d : donationRegulars) {
            DonationRegularFindResponseDto dto = DonationRegularFindResponseDto.builder()
                    .patronUsername(d.getPatronName())
                    .artistUsername(d.getArtistName())
                    .executeDay(d.getExecuteDay())
                    .amount(d.getAmount())
                    .anonymous(d.isAnonymous())
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    public void donationRegular(DonationRegularRequestDto dto) {

        JobKey jobKey = makeJobKey(dto.patronName(), dto.artistName());

        // check jobkey duplication
        checkJobKeyDuplication(jobKey);
        // check execute day validation
        checkValidExecuteDay(dto.executeDay());

        DonationRegular donationRegular = saveDonationRegularEntity(dto, jobKey);
        JobDetail jobDetail = makeJobDetail(jobKey, donationRegular);
        Trigger trigger = makeTrigger(jobKey, donationRegular);

        makeSchedule(jobDetail, trigger);
    }

    public void updateExecuteDay(DonationRegularUpdateRequestDto updateRequestDto) {
        JobKey jobKey = makeJobKey(updateRequestDto.patronName(), updateRequestDto.artistName());

        DonationRegular donationRegular = checkAndGetDonationRegularByJobKey(jobKey);
        donationRegular = donationRegular.toBuilder().executeDay(updateRequestDto.executeDay()).build();
        donationRegularRepository.save(donationRegular);

        Trigger trigger = triggerService.update(scheduler, jobKey, updateRequestDto.executeDay());

        try {
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } catch (SchedulerException e) {
            throw new QuartzJobSchedulerNotUpdatedException();
        }
    }

    public void deleteDonationRegularJob(DonationRegularUpdateRequestDto updateRequestDto){
        JobKey jobKey = makeJobKey(updateRequestDto.patronName(), updateRequestDto.artistName());
        DonationRegular donationRegular = checkAndGetDonationRegularByJobKey(jobKey);
        try {
            scheduler.deleteJob(jobKey);
            donationRegularRepository.delete(donationRegular);
        }catch (SchedulerException sc){
            throw new QuartzJobNotDeletedException();
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

    private JobDetail makeJobDetail(JobKey jobKey, DonationRegular donationRegular){
        return jobDetailService.build(jobKey, donationRegular);
    }

    private Trigger makeTrigger(JobKey jobKey, DonationRegular donationRegular) {
        return triggerService.build(jobKey, donationRegular);
    }

    private void checkJobKeyDuplication(JobKey jobKey){
        Optional<DonationRegular> donationRegularOp = donationRegularRepository.findByJobKey(jobKey);
        donationRegularOp.ifPresent(exception ->{
            throw new JobKeyDuplicationException();
        });
    }

    private DonationRegular checkAndGetDonationRegularByJobKey(JobKey jobKey) {

        Optional<DonationRegular> donationRegularOp = donationRegularRepository.findByJobKey(jobKey);

        if (donationRegularOp.isPresent()) {
            return donationRegularOp.get();
        } else {
            throw new DonationRegularNotFoundException();
        }
    }

    private void checkValidExecuteDay(Integer day){
        if(day == null){
            throw new RuntimeException("후원 실행 날짜가 없습니다.");
        }
        if(day <= 0 || day > 31){
            throw new RuntimeException("후원 실행 날짜가 잘못되었습니다.");
        }
    }
}
