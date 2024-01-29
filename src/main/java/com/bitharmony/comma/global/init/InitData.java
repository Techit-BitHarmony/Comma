package com.bitharmony.comma.global.init;

import com.bitharmony.comma.domain.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.domain.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.domain.credit.withdraw.service.WithdrawService;
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
    private final WithdrawService withdrawService;


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

                withdrawService.applyWithdraw("신한은행", "11111111", 10000);
                withdrawService.applyWithdraw("우리은행", "22222222", 20000);
                withdrawService.applyWithdraw("하나은행", "33333333", 30000);
                withdrawService.applyWithdraw("국민은행", "44444444", 40000);

            }
        };
    }
}
