package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.dto.DonationRegularRequsetDto;
import com.mchange.v2.cfg.PropertiesConfigSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class DonationRegularTriggerService {

    public Trigger build(JobKey jobKey, DonationRegularRequsetDto dto) {
        log.info("trigger 설정");

        CronExpression cronExpression = makeCronExpression(dto.executeDay());
        if (cronExpression == null) {
            // null 처리
        }
        return newTrigger()
                .forJob(jobKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // 정기 결제 주기마다 실행을 시킨다.
                .withIdentity(new TriggerKey(jobKey.getName(), jobKey.getGroup()))
                .startAt(new Date()) //다음 배송일에 결제를 시작한다.
                .build();
    }

    public CronExpression makeCronExpression(int day) {
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(String.format("0 0 9 %s 1/1 ? *", day));
        } catch (ParseException e) {
            // exception 처리 추가 필요
            throw new RuntimeException(e);
        }

        return cronExpression;
    }

//    public Trigger retryTrigger() {
//        log.info("retry trigger 설정");
//        return newTrigger()
//                .withSchedule(simpleSchedule()
//                        .withIntervalInHours(24)
//                        .withRepeatCount(3)
//                )
//                .startAt(futureDate(10, MINUTE))
//                .withIdentity(new TriggerKey("retry"))
//                .build();
//    }
}
