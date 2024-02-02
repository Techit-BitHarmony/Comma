package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationOnceRequestDto;
import com.bitharmony.comma.donation.dto.DonationResponse;
import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.entity.DonationRegular;
import com.bitharmony.comma.donation.repository.DonationRepository;
import com.bitharmony.comma.global.exception.CreditShortageException;
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
    private final DonationRepository donationRespository;
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
        donationRespository.save(donation);

        //후원자의 크레딧을 아티스트에게 전달
        countCredit(patron, artist, dto.amount());

        // 결과 반환

    }

    public void donateRegularToArtist(DonationRegular donationRegular) {
        Member patron = memberService.getMemberByUsername(donationRegular.getPatronName());
        Member artist = memberService.getMemberByUsername(donationRegular.getArtistName());

        //credit 부족한지 확인
        checkCredit(patron, donationRegular.getAmount());

        //donation 엔티티 생성 및 저장
        Donation donation = Donation.builder()
                .artistUsername(artist.getUsername())
                .patron(patron)
                .amount(donationRegular.getAmount())
                .message("")
                .anonymous(donationRegular.isAnonymous())
                .build();
        donationRespository.save(donation);

        //후원자의 크레딧을 아티스트에게 전달
        countCredit(patron, artist, donationRegular.getAmount());
    }

    // donation에서 memberRepostiory에 접근 하는 방식이 옳은가?
    private void countCredit(Member patron, Member artist, Integer amount) {
        patron = patron.toBuilder().credit(patron.getCredit() - amount).build();
        artist = artist.toBuilder().credit(artist.getCredit() + amount).build();

        memberRepository.save(patron);
        memberRepository.save(artist);
    }

    private void checkCredit(Member patron, Integer amount) {
        if (patron.getCredit() < amount) {
            // 후원 프로세스 멈추고 금액부족 메세지 반환
            throw new CreditShortageException("크레딧이 부족합니다.");
        }
    }

    // 회원 인증 필요
    // 아티스트가 받은 내역 조회 할 때
    public DonationResponse getDonationListByArtistUsername(String username) {

        List<Donation> donationList = donationRespository.findAllByArtistUsername(username);
        List<DonationFindResponseDto> responseDtoList = new ArrayList<>();

        if (donationList.size() == 0) {
            return DonationResponse.of("후원내역 없음.");
        }

        for (Donation d : donationList) {
            DonationFindResponseDto dto = DonationFindResponseDto.builder()
                    .patronUsername(checkDonationAnonymousAndGetPatronName(d))
                    .amount(d.getAmount())
                    .message(d.getMessage())
                    .time(d.getCreateDate())
                    .build();

            responseDtoList.add(dto);
        }
        return DonationResponse.of("후원내역 반환.", responseDtoList);
    }

    // 회원 인증 필요
    // 후원자가 준 내역 확인 할 때
    public DonationResponse getDonationListByPatron(Member patron) {

        List<Donation> donationList = donationRespository.findAllByPatron(patron);
        List<DonationFindResponseDto> responseDtoList = new ArrayList<>();

        if (donationList.size() == 0) {
            return DonationResponse.of("후원내역 없음.");
        }

        for (Donation d : donationList) {
            DonationFindResponseDto dto = DonationFindResponseDto.builder()
                    .artistUsername(d.getArtistUsername())
                    .amount(d.getAmount())
                    .message(d.getMessage())
                    .time(d.getCreateDate())
                    .build();

            responseDtoList.add(dto);
        }
        return DonationResponse.of("후원내역 반환.", responseDtoList);
    }

    private String checkDonationAnonymousAndGetPatronName(Donation donation) {
        if (donation.isAnonymous()) {
            return "익명";
        }
        return donation.getPatron().getUsername();
    }
}
