package com.bitharmony.comma.credit.creditLog.controller;

import com.bitharmony.comma.credit.creditLog.dto.CreditLogDto;
import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetListResponse;
import com.bitharmony.comma.credit.creditLog.dto.CreditLogGetResponse;
import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
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
@RequestMapping("/credit")
public class CreditLogController {

    private final CreditLogService creditLogService;
    private final MemberService memberService;

    @GetMapping("/creditlogs/{id}")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreditLogGetListResponse> getMyCreditLogs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Principal principal) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Member member = memberService.getMemberByUsername(principal.getName());

        Page<CreditLog> creditLogs = creditLogService.getMyCreditLogs(member.getId(), pageable);

        return new ResponseEntity<>(
                CreditLogGetListResponse.builder()
                        .restCredit(member.getCredit())
                        .creditLogDtos(creditLogs.map(CreditLogDto::new))
                        .build(),
                HttpStatus.OK);
    }
}
