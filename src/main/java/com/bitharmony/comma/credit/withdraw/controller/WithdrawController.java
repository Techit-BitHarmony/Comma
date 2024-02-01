package com.bitharmony.comma.credit.withdraw.controller;

import com.bitharmony.comma.credit.withdraw.service.WithdrawService;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetListResponse;
import com.bitharmony.comma.credit.withdraw.dto.WithdrawGetResponse;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credit")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;

    @GetMapping("/withdraws/{id}")
    public ResponseEntity<WithdrawGetResponse> getWithdraw(@PathVariable long id){
        WithdrawGetResponse withdrawGetResponse = new WithdrawGetResponse(this.withdrawService.getWithdraw(id));

        return new ResponseEntity<>(withdrawGetResponse, HttpStatus.OK);
    }

    @GetMapping("/withdraws")
    public ResponseEntity<WithdrawGetListResponse> getWithdrawList(){
        WithdrawGetListResponse withdrawGetListResponse =
                WithdrawGetListResponse.toDtoList(this.withdrawService.getWithdrawList());

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
