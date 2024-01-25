package com.bitharmony.comma.global.init;

import com.bitharmony.comma.domain.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.domain.credit.creditLog.service.CreditLogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.ApplicationArguments;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class InitData {

    private final ChargeService chargeService;
    private final ChargeRepository chargeRepository;
    private final CreditLogService creditLogService;


    @Bean
    public ApplicationRunner run() {
        return new ApplicationRunner() {
            @Override
            @Transactional
            @SneakyThrows
            public void run(ApplicationArguments args) {

                if(chargeRepository.count() > 0) { return; }

                chargeService.createCharge(10000);
                chargeService.createCharge(20000);
                chargeService.createCharge(30000);
                chargeService.createCharge(40000);

                creditLogService.addCreditLog(CreditLog.EventType.충전__토스페이먼츠, 10000);
                creditLogService.addCreditLog(CreditLog.EventType.충전__토스페이먼츠, 20000);
                creditLogService.addCreditLog(CreditLog.EventType.충전__토스페이먼츠, 30000);
                creditLogService.addCreditLog(CreditLog.EventType.충전__토스페이먼츠, 40000);

            }
        };
    }
}
