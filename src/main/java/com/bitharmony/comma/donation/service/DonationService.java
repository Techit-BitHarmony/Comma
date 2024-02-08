package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationOnceRequestDto;
import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.repository.DonationRepository;
import com.bitharmony.comma.global.exception.Donation.CreditShortageException;
import com.bitharmony.comma.global.exception.Donation.DonationListNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public void donateOnceToArtist(DonationOnceRequestDto dto) {
        Member patron = memberService.getMemberByUsername(dto.patronName());
        Member artist = memberService.getMemberByUsername(dto.artistName());

        //credit 부족한지 확인
        checkCredit(patron, dto.amount());

        //donation 엔티티 생성 및 저장
        Donation donation = Donation.builder()
                .artistUsername(artist.getUsername())
                .patron(patron)
                .amount(dto.amount())
                .message(dto.message())
                .anonymous(dto.anonymous())
                .build();
        donationRepository.save(donation);

        //후원자의 크레딧을 아티스트에게 전달
        countCredit(patron, artist, dto.amount());
    }

    private void countCredit(Member patron, Member artist, Long amount) {
        memberService.updateCredit(patron.getUsername(), patron.getCredit() - amount);
        memberService.updateCredit(artist.getUsername(), artist.getCredit() + amount);
    }

    private void checkCredit(Member patron, Long amount) {
        if (patron.getCredit() < amount) {
            // 후원 프로세스 멈추고 금액부족 메세지 반환
            throw new CreditShortageException();
        }
    }

    // 회원 인증 필요
    // 아티스트가 받은 내역 조회 할 때
    public List<DonationFindResponseDto> getDonationListByArtistUsername(String username) {

        List<Donation> donationList = donationRepository.findAllByArtistUsername(username);
        List<DonationFindResponseDto> responseDtoList = new ArrayList<>();

        if (donationList.size() == 0) {
            throw new DonationListNotFoundException();
        }

        for (Donation d : donationList) {
            DonationFindResponseDto dto = DonationFindResponseDto.builder()
                    .patronUsername(checkDonationAnonymousAndGetPatronName(d))
                    .artistUsername(d.getArtistUsername())
                    .amount(d.getAmount())
                    .message(d.getMessage())
                    .time(d.getCreateDate())
                    .build();

            responseDtoList.add(dto);
        }
        return responseDtoList;
    }

    // 회원 인증 필요
    // 후원자가 준 내역 확인 할 때
    public List<DonationFindResponseDto> getDonationListByPatronUsername(String username) {

        Member patron = memberService.getMemberByUsername(username);
        List<Donation> donationList = donationRepository.findAllByPatron(patron);
        List<DonationFindResponseDto> responseDtoList = new ArrayList<>();

        if (donationList.size() == 0) {
            throw new DonationListNotFoundException();
        }

        for (Donation d : donationList) {
            DonationFindResponseDto dto = DonationFindResponseDto.builder()
                    .patronUsername(d.getPatron().getUsername())
                    .artistUsername(d.getArtistUsername())
                    .amount(d.getAmount())
                    .message(d.getMessage())
                    .time(d.getCreateDate())
                    .build();

            responseDtoList.add(dto);
        }
        return responseDtoList;
    }

    private String checkDonationAnonymousAndGetPatronName(Donation donation) {
        if (donation.isAnonymous()) {
            return "익명";
        }
        return donation.getPatron().getUsername();
    }

    public void testSetCredit(String patron, String artist){
        Member member1 = memberService.getMemberByUsername(patron);
        Member member2 = memberService.getMemberByUsername(artist);

        memberRepository.save(member1.toBuilder().credit(10000L).build());
        memberRepository.save(member2.toBuilder().credit(10000L).build());
    }
}
