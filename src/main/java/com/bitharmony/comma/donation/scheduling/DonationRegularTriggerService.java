package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.entity.DonationRegular;
import com.mchange.v2.cfg.PropertiesConfigSource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class DonationRegularTriggerService {

    public Trigger build(JobKey jobKey, DonationRegular donationRegular) {
        log.info("trigger 설정");

        CronExpression cronExpression = makeCronExpression(donationRegular.getExecuteDay());
        if (cronExpression == null) {
            // null 처리
        }
        return newTrigger()
                .forJob(jobKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // 정기 결제 주기마다 실행을 시킨다.
                .withIdentity(new TriggerKey(jobKey.getName(), jobKey.getGroup()))
                .startNow()
                .build();
    }

    private CronExpression makeCronExpression(int day) {
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(String.format("0 0 10 %s L ?", day));
        } catch (ParseException e) {
            // exception 처리 추가 필요
            throw new RuntimeException(e);
        }

        return cronExpression;
    }

    public Trigger update(Scheduler scheduler, JobKey jobKey, Integer day){
        // 트리거 키 생성
        TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());

        // cron trigger 형태로 불러오기?? 이게 가능?
        CronTriggerImpl trigger = new CronTriggerImpl();
        try{
            trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e){
            // 커스텀 예외
        }

        //크론 표현식 정의
        CronExpression cronExpression = makeCronExpression(day);

        // 새로운 날짜로 트리거 업데이트
        trigger.setCronExpression(cronExpression);

        return trigger;
    }

}
