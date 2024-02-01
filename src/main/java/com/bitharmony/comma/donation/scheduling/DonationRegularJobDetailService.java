package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.dto.DonationRegularRequsetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@RequiredArgsConstructor
@Component
public class DonationRegularJobDetailService {
    public JobDetail build(JobKey jobKey, Long donationId, DonationRegularRequsetDto dto) throws IllegalAccessException {
        log.warn("job detail donationId= {}", donationId);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("donationId", donationId);
        jobDataMap.put("retry", 0);
        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(dto);

            jobDataMap.put(field.getName(),value);
        }

        return newJob(DonationRegularJob.class)
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .storeDurably(true)
                .usingJobData(jobDataMap)
                .build();
    }
}
