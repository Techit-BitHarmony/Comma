package com.bitharmony.comma.global.init;

import com.bitharmony.comma.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.credit.charge.service.ChargeService;
import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
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
    private final MemberService memberService;


    @Bean
    public ApplicationRunner run() {
        return new ApplicationRunner() {
            @Override
            @Transactional
            @SneakyThrows
            public void run(ApplicationArguments args) {

            }
        };
    }
}
