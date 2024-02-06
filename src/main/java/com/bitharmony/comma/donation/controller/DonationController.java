package com.bitharmony.comma.donation.controller;

import com.bitharmony.comma.donation.dto.DonationFindResponseDto;
import com.bitharmony.comma.donation.dto.DonationOnceRequestDto;
import com.bitharmony.comma.donation.dto.DonationRegularRequestDto;
import com.bitharmony.comma.donation.dto.DonationRegularUpdateRequestDto;
import com.bitharmony.comma.donation.service.DonationRegularService;
import com.bitharmony.comma.donation.service.DonationService;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donation")
@Slf4j
public class DonationController {

    private final DonationService donationService;
    private final DonationRegularService donationRegularService;

    // 커스텀 예외 적용 필요
    private void checkLoginUser(Principal principal, String username) {
        try {
            String loginedUsername = principal.getName();
            System.out.println(loginedUsername);
            if (!loginedUsername.equals(username)) {
                throw new NotAuthorizedException();
            }
        } catch (Exception e) {
            throw new NotAuthorizedException();
        }
    }

    @GetMapping("/list/patron/{patronUsername}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<List<DonationFindResponseDto>> getAllDonationListToArtist(@PathVariable("patronUsername") String patronUsername, Principal principal) {

        checkLoginUser(principal, patronUsername);

        return GlobalResponse.of("200", donationService.getDonationListByPatronUsername(patronUsername));
    }

    @GetMapping("/list/artist/{artistUsername}")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<List<DonationFindResponseDto>> getAllDonationListFromPatron(@PathVariable String artistUsername, Principal principal) {

        checkLoginUser(principal, artistUsername);

        return GlobalResponse.of("200", donationService.getDonationListByArtistUsername(artistUsername));
    }

    @PostMapping("/once")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse donationOnce(@RequestBody DonationOnceRequestDto dto, Principal principal) {

        checkLoginUser(principal, dto.patronName());

        donationService.donateOnceToArtist(dto);

        return GlobalResponse.of("200");
    }

    @PostMapping("/regular")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse donationRegular(@RequestBody DonationRegularRequestDto dto, Principal principal) {
        log.info("donation regular");
        checkLoginUser(principal, dto.patronName());

        donationRegularService.donationRegular(dto);

        return GlobalResponse.of("200");
    }

    @PostMapping("/regular/update")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse modifyDonationRegular(@RequestBody DonationRegularUpdateRequestDto updateRequestDto, Principal principal) {

        checkLoginUser(principal, updateRequestDto.patronName());

        donationRegularService.updateExecuteDay(updateRequestDto);

        return GlobalResponse.of("200");
    }

    @PostMapping("/test/{user1}/{user2}")
    public GlobalResponse test(@PathVariable("user1") String user1, @PathVariable("user2") String user2){
        donationService.testSetCredit(user1,user2);

        return GlobalResponse.of("200");
    }

}
