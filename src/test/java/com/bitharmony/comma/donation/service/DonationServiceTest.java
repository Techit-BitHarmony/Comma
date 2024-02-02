package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationOnceRequestDto;
import com.bitharmony.comma.donation.dto.DonationRegularRequsetDto;
import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.repository.DonationRepository;
import com.bitharmony.comma.member.dto.MemberJoinRequest;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import com.bitharmony.comma.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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
        memberRepository.save(user1.toBuilder().credit(10000).build());
        memberRepository.save(user2.toBuilder().credit(0).build());
    }

    @Test
    public void donationOnceTest() {
        DonationOnceRequestDto dto = DonationOnceRequestDto.builder()
                .patronName("user1")
                .artistName("user2")
                .amount(1000)
                .message("testMessage")
                .anonymous(false)
                .build();
        donationService.donateOnceToArtist(dto);
        Member user1 = memberService.getMemberByUsername("user1");
        Assertions.assertThat(user1.getCredit()).isEqualTo(9000);
    }
    @Test
    public void donationRegularTest1() throws SchedulerException {
        DonationRegular donationRegular = DonationRegular.builder()
                .patronName("user1")
                .artistName("user2")
                .amount(1000)
                .anonymous(false)
                .executeDay(2)
                .build();
        donationRegularService.makeSchedule(donationRegular);
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