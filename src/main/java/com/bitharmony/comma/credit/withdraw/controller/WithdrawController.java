package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.dto.WithdrawDto;
import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetListResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetResponse;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/credit")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;
    private MemberService memberService;

    @GetMapping("/withdraws/{id}")
    public ResponseEntity<WithdrawGetResponse> getWithdraw(@PathVariable long id){
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
    public ResponseEntity<WithdrawGetListResponse> getMyWithdrawList(){

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


    @DeleteMapping("/withdraws/{withdrawId}")
    public ResponseEntity<Void> deleteWithdraw(@PathVariable long withdrawId){
        Withdraw withdraw = withdrawService.getWithdraw(withdrawId);

        // 멤버 기능 연동시 수정
//        if(!withdrawService.canDelete(rq.getMember(), withdraw)){
//            throw new RuntimeException("출금 신청을 취소할 수 없습니다.");
//        }

        withdrawService.delete(withdrawId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
