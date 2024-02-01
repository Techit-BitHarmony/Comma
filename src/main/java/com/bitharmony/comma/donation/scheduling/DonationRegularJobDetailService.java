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
    public JobDetail build(JobKey jobKey, DonationRegularRequsetDto dto) {
        log.warn("job detail name= {}", jobKey.getName());

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("dto", dto);
        jobDataMap.put("retry", 0);
//        for (Field field : dto.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            Object value = field.get(dto);
//
//            jobDataMap.put(field.getName(),value);
//        }

        return newJob(DonationRegularJob.class)
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .storeDurably(true)
                .usingJobData(jobDataMap)
                .build();
    }
}
