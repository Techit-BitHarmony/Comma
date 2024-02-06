package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.entity.DonationRegular;
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
    public JobDetail build(JobKey jobKey, DonationRegular donationRegular)  {
        log.warn("job detail name= {}", jobKey.getName());

        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("donationRegular", donationRegular);
        jobDataMap.put("retry", 0);
        jobDataMap.put("patronName", donationRegular.getPatronName());
        jobDataMap.put("artistName", donationRegular.getArtistName());
        jobDataMap.put("amount", donationRegular.getAmount());
        jobDataMap.put("anonymous", donationRegular.isAnonymous());
//        for (Field field : donationRegular.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            Object value = field.get(donationRegular);
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
