package com.bitharmony.comma.donation.controller;

import com.bitharmony.comma.donation.dto.*;
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
@PreAuthorize("isAuthenticated()")
public class DonationController {

    private final DonationService donationService;
    private final DonationRegularService donationRegularService;

    private void checkLoginUser(Principal principal, String username) {
        try {
            String loginUsername = principal.getName();
            System.out.println(loginUsername);
            if (!loginUsername.equals(username)) {
                throw new NotAuthorizedException();
            }
        } catch (Exception e) {
            throw new NotAuthorizedException();
        }
    }

    @GetMapping("/list/patron/{patronUsername}")
    public GlobalResponse<List<DonationFindResponseDto>> getAllDonationListToArtist(
            @PathVariable("patronUsername") String patronUsername,
            Principal principal) {

        checkLoginUser(principal, patronUsername);

        return GlobalResponse.of("200", donationService.getDonationListByPatronUsername(patronUsername));
    }

    @GetMapping("/list/artist/{artistUsername}")
    public GlobalResponse<List<DonationFindResponseDto>> getAllDonationListFromPatron(
            @PathVariable String artistUsername,
            Principal principal) {

        checkLoginUser(principal, artistUsername);

        return GlobalResponse.of("200", donationService.getDonationListByArtistUsername(artistUsername));
    }
    @GetMapping("/list/patron/{patronUsername}/regular")
    public GlobalResponse<List<DonationRegularFindResponseDto>> getAllDonationRegularList(
            @PathVariable String patronUsername,
            Principal principal
    ){
        checkLoginUser(principal, patronUsername);

        return GlobalResponse.of("200",donationRegularService.getAllDonationRegularList(patronUsername));
    }

    @PostMapping("/once")
    public GlobalResponse donationOnce(
            @RequestBody DonationOnceRequestDto dto,
            Principal principal) {

        checkLoginUser(principal, dto.patronName());

        donationService.donateOnceToArtist(dto);

        return GlobalResponse.of("200", "후원이 완료되었습니다.");
    }

    @PostMapping("/regular")
    public GlobalResponse donationRegular(
            @RequestBody DonationRegularRequestDto dto,
            Principal principal) {

        checkLoginUser(principal, dto.patronName());

        donationRegularService.donationRegular(dto);

        return GlobalResponse.of("200", "정기후원이 등록되었습니다.");
    }

    @PatchMapping("/regular")
    public GlobalResponse modifyDonationRegular(
            @RequestBody DonationRegularUpdateRequestDto updateRequestDto,
            Principal principal) {

        checkLoginUser(principal, updateRequestDto.patronName());

        donationRegularService.updateExecuteDay(updateRequestDto);

        return GlobalResponse.of("200", "정기후원일이 변경되었습니다.");
    }

    @DeleteMapping("/regular")
    public GlobalResponse deleteDonationRegular(
            @RequestBody DonationRegularUpdateRequestDto updateRequestDto,
            Principal principal) {

        checkLoginUser(principal, updateRequestDto.patronName());

        donationRegularService.deleteDonationRegularJob(updateRequestDto);

        return GlobalResponse.of("200", "정기후원이 종료되었습니다.");
    }

    @PostMapping("/test/{user1}/{user2}")
    public GlobalResponse test(
            @PathVariable("user1") String user1,
            @PathVariable("user2") String user2) {

        donationService.testSetCredit(user1, user2);

        return GlobalResponse.of("200");
    }

}
