package com.bitharmony.comma.donation.scheduling;

import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.repository.DonationRepository;
import com.bitharmony.comma.donation.service.DonationService;
import com.bitharmony.comma.global.exception.Donation.CreditShortageException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@DisallowConcurrentExecution // 중복 실행 방지
@PersistJobDataAfterExecution
public class DonationRegularJob implements Job {
    private final DonationRepository donationRepository;
    private final MemberService memberService;


    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
//        DonationRegular donationRegular = (DonationRegular) mergedJobDataMap.get("donationRegular");
        DonationRegular donationRegular = DonationRegular.builder()
                .patronName( mergedJobDataMap.getString("patronName"))
                .artistName(mergedJobDataMap.getString("artistName"))
                .amount(mergedJobDataMap.getLong("amount"))
                .anonymous(mergedJobDataMap.getBoolean("anonymous"))
                .build();
        log.info("job execute~~~~");
        Member patron = memberService.getMemberByUsername(donationRegular.getPatronName());
        Member artist = memberService.getMemberByUsername(donationRegular.getArtistName());

        //credit 부족 확인
        checkCredit(patron, donationRegular.getAmount());

        //donation 엔티티 생성 및 저장
        Donation donation = Donation.builder()
                .artistUsername(artist.getUsername())
                .patron(patron)
                .amount(donationRegular.getAmount())
                .message("")
                .anonymous(donationRegular.isAnonymous())
                .build();
        donationRepository.save(donation);

        //후원자의 크레딧을 아티스트에게 전달
        countCredit(patron, artist, donationRegular.getAmount());

    }

    private void checkCredit(Member patron, Long amount) {
        if (patron.getCredit() < amount) {
            // 후원 프로세스 멈추고 금액부족 메세지 반환
            throw new CreditShortageException();
        }
    }

    private void countCredit(Member patron, Member artist, Long amount) {
        memberService.updateCredit(patron.getUsername(), patron.getCredit() - amount);
        memberService.updateCredit(artist.getUsername(), artist.getCredit() + amount);
    }
}
