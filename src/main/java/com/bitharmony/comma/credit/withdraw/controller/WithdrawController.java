package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.dto.*;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;
    private final MemberService memberService;

    @GetMapping("/withdraws/{id}")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WithdrawGetListResponse> getMyWithdrawList(
            @RequestParam(value="page", defaultValue = "1") int page,
            Principal principal) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 5, Sort.by(sorts));

        Member member = memberService.getMemberByUsername(principal.getName());
        Page<Withdraw> withdraws = withdrawService.getMyWithdrawList(member.getId(), pageable);

        WithdrawGetListResponse withdrawGetListResponse =
                WithdrawGetListResponse.builder()
                        .withdraws(withdraws)
                        .build();

        return new ResponseEntity<>(withdrawGetListResponse, HttpStatus.OK);
    }

    @PostMapping("/withdraws")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WithdrawApplyResponse> applyWithdraw(
            @RequestBody WithdrawApplyRequest request, Principal principal) {

        Member member = memberService.getMemberByUsername(principal.getName());
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelWithdraw(@PathVariable long withdrawId, Principal principal) {

        Withdraw withdraw = withdrawService.getWithdraw(withdrawId);
        Member member = memberService.getMemberByUsername(principal.getName());

        withdrawService.cancelWithdraw(member, withdraw);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/withdraws/{withdrawId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WithdrawModifyResponse> modifyWithdraw(
            @PathVariable long withdrawId, @RequestBody WithdrawModifyRequest request
    ){
        Withdraw withdraw = withdrawService.getWithdraw(withdrawId);
        Withdraw _withdraw = withdrawService.modifyWithdraw(withdraw, request.bankName(), request.bankAccountNo(), request.withdrawAmount());

        WithdrawModifyResponse withdrawModifyResponse = WithdrawModifyResponse.builder()
                .bankName(_withdraw.getBankName())
                .bankAccountNo(_withdraw.getBankAccountNo())
                .withdrawAmount(_withdraw.getWithdrawAmount())
                .build();

        return new ResponseEntity<>(withdrawModifyResponse, HttpStatus.OK);
    }
}
