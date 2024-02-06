package com.bitharmony.comma.member.controller;

import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.dto.MemberJoinRequest;
import com.bitharmony.comma.member.dto.MemberLoginRequest;
import com.bitharmony.comma.member.dto.MemberModifyRequest;
import com.bitharmony.comma.member.dto.MemberReturnResponse;
import com.bitharmony.comma.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * Redis에 로그인된 사용자의 refresh token이 있는지 확인한다. 있으면 delete를 하고
     * 없으면 로그인이 안되는 것이므로 로그아웃을 할 수 없다. 이후, spring security 에서
     * jwt를 검증할 때 Redis에 refresh token이 존재하는지도 확인한다.
     */
    @PostMapping("/logout")
    public GlobalResponse logout() {
        memberService.logout();
        return GlobalResponse.of("200");
    }

    /**
     * /member/{userId or username} 으로 넘어오는 것이 아닌 해당 url로 요청을 하면
     * 로그인된 사용자의 정보를 DB에서 조회하여 JSON 형식으로 반환한다.
     */
    @GetMapping("/profile")
    public GlobalResponse profile() {
        MemberReturnResponse response = memberService.getProfile();
        return GlobalResponse.of("200", response);
    }

    /**
     * nickname이랑 email을 json 형태로 입력받아서 param으로 받는다.
     */
    @PutMapping("/modify")
    public GlobalResponse modify(@RequestBody @Valid MemberModifyRequest memberModifyRequest) {
        memberService.modify(memberModifyRequest.nickname(), memberModifyRequest.email());
        return GlobalResponse.of("200");
    }
}
