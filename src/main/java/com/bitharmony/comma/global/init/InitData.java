package com.bitharmony.comma.global.init;

import com.bitharmony.comma.domain.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.domain.credit.charge.service.ChargeService;
import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.domain.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.domain.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.member.repository.MemberRepository;
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

    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @Bean
    public ApplicationRunner run() {
        return new ApplicationRunner() {
            @Override
            @Transactional
            @SneakyThrows
            public void run(ApplicationArguments args) {

                if (memberRepository.count() > 0) return;

                memberService.join("user1", "1234", "user1@abc.com", "nickname1");
                memberService.join("user2", "1234", "user2@abc.com", "nickname2");
                memberService.join("user3", "1234", "user3@abc.com", "nickname3");


            }
        };
    }
}
