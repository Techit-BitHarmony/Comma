package com.bitharmony.comma.donation.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class DonationRegularTriggerService {

    public Trigger build(JobKey jobKey) {
        log.info("trigger 설정");
        return newTrigger()
                .forJob(jobKey)
                .withSchedule(calendarIntervalSchedule().withIntervalInDays(1)) // 정기 결제 주기마다 실행을 시킨다.
                .withIdentity(new TriggerKey(jobKey.getName(), jobKey.getGroup()))
                .startAt(new Date()) //다음 배송일에 결제를 시작한다.
                .build();
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
