package com.bitharmony.comma.credit.creditLog.controller;

import com.bitharmony.comma.credit.creditLog.dto.CreditLogDto;
import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetListResponse;
import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetResponse;
import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credit")
public class CreditLogController {

    private final CreditLogService creditLogService;
    private final MemberService memberService;

    @GetMapping("/creditlogs/{id}")
    public ResponseEntity<CreditLogGetResponse> getCreditLog(@PathVariable long id) {
        CreditLog creditLog = this.creditLogService.getCreditLogById(id);

        return new ResponseEntity<>(
                CreditLogGetResponse.builder()
                        .eventType(creditLog.getEventType())
                        .creditChangeAmount(creditLog.getCreditChangeAmount())
                        .restCredit(creditLog.getRestCredit())
                        .createDate(creditLog.getCreateDate())
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/creditlogs/mine")
    public ResponseEntity<CreditLogGetListResponse> getMyCreditLogs() {

        Member member = memberService.getMemberByUsername("user1");

        List<CreditLog> creditLogs = creditLogService.getMyCreditLogs(member.getId());

        return new ResponseEntity<>(
                CreditLogGetListResponse.builder()
                        .creditLogDtos(creditLogs.stream().map(CreditLogDto::new).toList())
                        .build(),
                HttpStatus.OK);
    }
}
