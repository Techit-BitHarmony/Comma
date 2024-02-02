package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.dto.WithdrawCancelResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawDoResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawDto;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetListResponse;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/credit")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminWithdrawController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;

    @GetMapping("/withdraws")
    public ResponseEntity<WithdrawGetListResponse> getAllWithdrawList() {

        List<Withdraw> withdraws = withdrawService.getAllWithdrawList();

        WithdrawGetListResponse withdrawGetListResponse =
                WithdrawGetListResponse.builder()
                        .withdrawDtos(
                                withdraws.stream()
                                        .map(WithdrawDto::new)
                                        .toList())
                        .build();

        return new ResponseEntity<>(withdrawGetListResponse, HttpStatus.OK);
    }

    @PutMapping("/withdraws/{id}/do")
    public ResponseEntity<WithdrawDoResponse> doWithdraw(@PathVariable long id) {
        Withdraw withdraw = withdrawService.doWithdraw(id);

        return new ResponseEntity<>(
                WithdrawDoResponse.builder()
                        .id(withdraw.getId())
                        .resultMsg(withdraw.getProcessResult())
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/withdraws/{id}/cancel")
    public ResponseEntity<WithdrawCancelResponse> cancelWithdraw(@PathVariable long id) {
        Withdraw withdraw = withdrawService.cancelWithdraw(id);

        return new ResponseEntity<>(
                WithdrawCancelResponse.builder()
                        .id(withdraw.getId())
                        .resultMsg(withdraw.getProcessResult())
                        .build(),
                HttpStatus.OK
        );
    }

}
