package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.dto.*;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;
    private final MemberService memberService;

    @GetMapping("/withdraws/{id}")
    public ResponseEntity<WithdrawGetResponse> getWithdraw(@PathVariable long id) {
        Withdraw withdraw = withdrawService.getWithdraw(id);

        WithdrawGetResponse withdrawGetResponse = WithdrawGetResponse.builder()
                .id(withdraw.getId())
                .applicantName(withdraw.getApplicant().getUsername())
                .bankName(withdraw.getBankName())
                .bankAcountNo(withdraw.getBankAccountNo())
                .withdrawAmount(withdraw.getWithdrawAmount())
                .applyDate(withdraw.getApplyDate())
                .build();

        return new ResponseEntity<>(withdrawGetResponse, HttpStatus.OK);
    }

    @GetMapping("/withdraws/mine")
    public ResponseEntity<WithdrawGetListResponse> getMyWithdrawList() {

        // TODO : 멤버 가져오는 메서드 추가시 수정 (임시로 user1 사용)
        Member member = memberService.getMemberByUsername("user1");
        List<Withdraw> withdraws = withdrawService.getMyWithdrawList(member.getId());

        WithdrawGetListResponse withdrawGetListResponse =
                WithdrawGetListResponse.builder()
                        .withdrawDtos(
                                withdraws.stream()
                                        .map(WithdrawDto::new)
                                        .toList())
                        .build();

        return new ResponseEntity<>(withdrawGetListResponse, HttpStatus.OK);
    }

    @PostMapping("/withdraws")
    public ResponseEntity<WithdrawApplyResponse> applyWithdraw(
            @RequestBody WithdrawApplyRequest request) {

        // TODO : 멤버 가져오는 메서드 추가시 수정 (임시로 user1 사용)
        Member member = memberService.getMemberByUsername("user1");
        Withdraw withdraw = withdrawService.applyWithdraw(
                member, request.bankName(), request.bankAccountNo(), request.withdrawAmount());

        return new ResponseEntity<>(
                WithdrawApplyResponse.builder()
                        .id(withdraw.getId())
                        .build(),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/withdraws/{withdrawId}")
    public ResponseEntity<Void> cancelWithdraw(@PathVariable long withdrawId) {
        Withdraw withdraw = withdrawService.getWithdraw(withdrawId);

        // TODO : 멤버 가져오는 메서드 추가시 수정 (임시로 user1 사용)
        Member member = memberService.getMemberByUsername("user1");

        withdrawService.delete(member, withdraw);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/withdraws/{withdrawId}")
    public ResponseEntity<WithdrawModifyResponse> modifyWithdraw(
            @PathVariable long withdrawId, @RequestBody WithdrawModifyRequest request){
        Withdraw withdraw = withdrawService.getWithdraw(withdrawId);
        Withdraw _withdraw = withdrawService.modifyWithdraw(withdraw, request.bankName(), request.bankAccountNo(), request.withdrawAmount());

        WithdrawModifyResponse withdrawModifyResponse = WithdrawModifyResponse.builder()
                .bankName(_withdraw.getBankName())
                .bankAccountNo(_withdraw.getBankName())
                .withdrawAmount(_withdraw.getWithdrawAmount())
                .build();

        return new ResponseEntity<>(withdrawModifyResponse, HttpStatus.OK);
    }
}
