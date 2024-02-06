package com.bitharmony.comma.member.controller;

import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.dto.MemberJoinRequest;
import com.bitharmony.comma.member.dto.MemberLoginRequest;
import com.bitharmony.comma.member.dto.MemberModifyRequest;
import com.bitharmony.comma.member.dto.MemberPwModifyRequest;
import com.bitharmony.comma.member.dto.MemberReturnResponse;
import com.bitharmony.comma.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public GlobalResponse login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return GlobalResponse.of("200",
                memberService.login(memberLoginRequest.username(), memberLoginRequest.password()));
    }

    @PostMapping("/join")
    public GlobalResponse join(@RequestBody @Valid MemberJoinRequest memberJoinRequest) {

        memberService.join(
                memberJoinRequest.username(), memberJoinRequest.password(),
                memberJoinRequest.email(), memberJoinRequest.nickname());

        return GlobalResponse.of("201");
    }

    @PostMapping("/logout")
    public GlobalResponse logout() {
        memberService.logout();
        return GlobalResponse.of("200");
    }


    @GetMapping("/mypage")
    public GlobalResponse mypage() {
        MemberReturnResponse response = memberService.getProfile();
        return GlobalResponse.of("200", response);
    }

    @GetMapping("/{username}")
    public GlobalResponse profile(@PathVariable("username") String username) {
        MemberReturnResponse response = memberService.getProfile(username);
        return GlobalResponse.of("200", response);
    }

    @PutMapping("/modify")
    public GlobalResponse modify(@RequestBody @Valid MemberModifyRequest memberModifyRequest) {
        memberService.modify(memberModifyRequest.nickname(), memberModifyRequest.email());
        return GlobalResponse.of("200");
    }

    @PutMapping("/passwordModify")
    public GlobalResponse passwordModify(@RequestBody @Valid MemberPwModifyRequest memberPwModifyRequest) {
        memberService.passwordModify(memberPwModifyRequest.passwordModify(),
                memberPwModifyRequest.passwordModifyCheck());

        return GlobalResponse.of("200");
    }
}
