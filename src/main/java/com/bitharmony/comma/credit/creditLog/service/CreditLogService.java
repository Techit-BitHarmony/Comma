package com.bitharmony.comma.credit.creditLog.service;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.repository.CreditLogRepository;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditLogService {

    private final CreditLogRepository creditLogRepository;


    public CreditLog getCreditLogById(long id){
        Optional<CreditLog> creditLog = this.creditLogRepository.findById(id);

        if (creditLog.isEmpty()) {
            throw new RuntimeException("존재하지 않는 크레딧 내역입니다.");
        }

        return creditLog.get();
    }

    public List<CreditLog> getCreditLogs() {
        return this.creditLogRepository.findAll();
    }

    public void addCreditLog(Member member, CreditLog.EventType eventType, long creditChangeAmount) {
        CreditLog creditLog = new CreditLog().builder()
                .member(member)
                .eventType(eventType)
                .creditChangeAmount(creditChangeAmount)
                .createDate(LocalDateTime.now())
                .restCredit(member.getCredit())
                .build();

        creditLogRepository.save(creditLog);
    }
}
