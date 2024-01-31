package com.bitharmony.comma.donation.service;

import com.bitharmony.comma.donation.dto.DonationFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationRequestDto;
import com.bitharmony.comma.donation.dto.DonationResponse;
import com.bitharmony.comma.donation.entity.Donation;
import com.bitharmony.comma.donation.repository.DonationRespository;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRespository donationRespository;
    private final MemberService memberService;

    public void donateOnceToArtist(DonationRequestDto dto){
        Member patron = memberService.getMemberByUsername(dto.patronName());
        Member artist = memberService.getMemberByUsername(dto.artistName());

        //donation entity create
        Donation donation = Donation.builder()
                .artistUsername(artist.getUsername())
                .patron(patron)
                .amount(dto.amount())
                .message(dto.message())
                .anonymous(dto.anonymous())
                .build();
        donationRespository.save(donation);

        //credit count
        checkCredit(patron,dto.amount());
    }

    public void checkCredit(Member patron, Integer amount){
        if(patron.getCredit() < amount){

        }
    }

    public DonationResponse getDonationListByArtistUsername(String username){

        List<Donation> donationList = donationRespository.findAllByArtistUsername(username);
        List<DonationFindResponseDto> responseDtoList = new ArrayList<>();

        if(donationList.size()==0){
            return DonationResponse.of("후원내역 없음.");
        }

        for (Donation d : donationList) {
            DonationFindResponseDto dto = DonationFindResponseDto.builder()
                    .patronUsername(checkDonationAnonymousAndGetPatronName(d))
                    .amount(d.getAmount())
                    .message(d.getMessage())
                    .build();

            responseDtoList.add(dto);
        }
        return DonationResponse.of("후원내역 반환.", responseDtoList);
    }

    public String checkDonationAnonymousAndGetPatronName(Donation donation){
        if(donation.isAnonymous()){
            return "익명";
        }
        return donation.getPatron().getUsername();
    }
}
