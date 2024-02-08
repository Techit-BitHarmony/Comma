package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationOnceRequestDto;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import com.bitharmony.comma.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DonationServiceTest {

    @Autowired
    private DonationService donationService;
    @Autowired
    private DonationRegularService donationRegularService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void memberCreate() {
        memberService.join("user1", "1234", "user1@test.com", "user1");
        memberService.join("user2", "1234", "user2@test.com", "user2");
        Member user1 = memberService.getMemberByUsername("user1");
        Member user2 = memberService.getMemberByUsername("user2");
        memberRepository.save(user1.toBuilder().credit(10000L).build());
        memberRepository.save(user2.toBuilder().credit(0L).build());
    }

    @Test
    public void donationOnceTest() {
        DonationOnceRequestDto dto = DonationOnceRequestDto.builder()
                .patronName("user1")
                .artistName("user2")
                .amount(1000L)
                .message("testMessage")
                .anonymous(false)
                .build();
        donationService.donateOnceToArtist(dto);
        Member user1 = memberService.getMemberByUsername("user1");
        Assertions.assertThat(user1.getCredit()).isEqualTo(9000L);
    }
    @Test
    public void donationRegularTest1() throws SchedulerException {
        DonationRegular donationRegular = DonationRegular.builder()
                .patronName("user1")
                .artistName("user2")
                .amount(1000L)
                .anonymous(false)
                .executeDay(2)
                .build();
//        donationRegularService.makeSchedule(donationRegular);
    }
//    @Test
//    public void donationRegularTest2() throws SchedulerException {
//        DonationRegularRequsetDto dto = DonationRegularRequsetDto.builder()
//                .patronName("user1")
//                .artistName("user2")
//                .amount(1000)
//                .anonymous(false)
//                .executeDay(2)
//                .build();
//        donationRegularService.makeSchedule(dto);
//    }

}