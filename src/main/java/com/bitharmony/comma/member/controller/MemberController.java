package com.bitharmony.comma.member.controller;

import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.dto.MemberJoinRequest;
import com.bitharmony.comma.member.dto.MemberLoginRequest;
import com.bitharmony.comma.member.dto.MemberReturnResponse;
import com.bitharmony.comma.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /**
     * 사용자가 username과 password를 입력하면, 유효성 검증을 거쳐 로그인을 로직을 실행하고, 정상적으로 처리가 되면 JWT을 respone body로 반환 클라이언트를 해당 JWT를 받아서 매
     * 요청마다 헤더로 포함해서 서버로 전송해야한다.
     */
    @PostMapping("/login")
    public GlobalResponse login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return GlobalResponse.of("200",
                memberService.login(memberLoginRequest.username(), memberLoginRequest.password()));
    }

    /**
     * 사용자가 회원가입에 필요한 데이터를 입력하면, 유효성 검증을 거쳐 회원가입 로직을 실행한다. 정상적으로 처리가 되면 회원가입한 정보의 일부를 클라이언트로 반환한다.
     */

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

    // TODO : /GET :/member/profile
    @GetMapping("/profile")
    public GlobalResponse profile() {
        MemberReturnResponse response = memberService.getProfile();
        return GlobalResponse.of("200", response);
    }

    // TODO : /PUT :/member/modify

    //TODO : 삭제예정(임시 테스트용)
    @GetMapping("/api/1")
    public String get11() {
        return "example입니다.";
    }
}
