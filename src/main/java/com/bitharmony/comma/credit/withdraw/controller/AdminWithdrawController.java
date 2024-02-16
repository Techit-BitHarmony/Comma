package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.dto.WithdrawCancelResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawDoResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawDto;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetListResponse;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/admin/credit")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminWithdrawController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;

    @GetMapping("/withdraws")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WithdrawGetListResponse> getAllWithdrawList(
            @RequestParam(value="page", defaultValue = "1") int page,
            Principal principal
            ) {
        if(!principal.getName().equals("admin")){
            throw new NotAuthorizedException();
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Withdraw> withdraws = withdrawService.getAllWithdrawList(pageable);

        WithdrawGetListResponse withdrawGetListResponse =
                WithdrawGetListResponse.builder()
                        .withdraws(withdraws.map(WithdrawDto::new))
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

    @PutMapping("/withdraws/{id}/reject")
    public ResponseEntity<WithdrawCancelResponse> cancelWithdraw(@PathVariable long id) {
        Withdraw withdraw = withdrawService.rejectWithdraw(id);

        return new ResponseEntity<>(
                WithdrawCancelResponse.builder()
                        .id(withdraw.getId())
                        .resultMsg(withdraw.getProcessResult())
                        .build(),
                HttpStatus.OK
        );
    }

}
