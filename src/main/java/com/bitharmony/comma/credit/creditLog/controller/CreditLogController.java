package com.bitharmony.comma.credit.creditLog.controller;

import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetListResponse;
import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetResponse;
import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credit")
public class CreditLogController {

    private final CreditLogService creditLogService;

    @GetMapping("/creditLogs/{id}")
    public ResponseEntity<CreditLogGetResponse> getCreditLog(@PathVariable long id){
        CreditLog creditLog = this.creditLogService.getCreditLogById(id);
        CreditLogGetResponse creditLogGetResponse = new CreditLogGetResponse(creditLog);

        return new ResponseEntity<>(creditLogGetResponse, HttpStatus.OK);
    }

    @GetMapping("/creditLogs")
    public ResponseEntity<CreditLogGetListResponse> getCreditLogs(){
        CreditLogGetListResponse creditLogGetListResponse =
                 CreditLogGetListResponse.toDtoList(this.creditLogService.getCreditLogs());

        return new ResponseEntity<>(creditLogGetListResponse, HttpStatus.OK);
    }


//    // 멤버 기능 추가시 구현 예정
//    @GetMapping("/creditLogs/member/{memberId}")
//    public ResponseEntity<CreditLogGetListByMemberIdResponse> getCreditLogsByMemberId(
//            @PathVariable long memberId
//    ){
//    }

}
