package com.bitharmony.comma.domain.credit.charge.service;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.domain.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final CreditLogService creditLogService;


    public Charge getChargeById(long id) {
        Optional<Charge> charge = this.chargeRepository.findById(id);

        if (charge.isEmpty()) {
            throw new RuntimeException("존재하지 않는 주문입니다.");
        }

        return charge.get();
    }

    public List<Charge> getChargeList() {
        return this.chargeRepository.findAll();
    }

    public Charge createCharge(Member member, long chargeAmount) {
        Charge charge = Charge.builder()
                .charger(member)
                .chargeAmount(chargeAmount)
                .build();
        this.chargeRepository.save(charge);

        return charge;
    }


    // 주문서와 결제요청의 정보가 일치하는지 확인하는 메서드
    public void checkValidity(String orderId, long amount) throws RuntimeException {
        long id = Long.parseLong(orderId.split("__", 2)[1]);
        Charge charge = chargeRepository.findById(id).orElse(null);

        if (charge == null) {
            throw new RuntimeException("존재하지 않는 주문입니다.");
        }

        if (charge.getChargeAmount() != amount) {
            throw new RuntimeException("금액이 일치하지 않습니다.");
        }
    }


    // 결제 성공시 멤버의 크레딧을 증가시키고 크레딧 로그를 남기는 메서드 (추후 멤버 기능 연동시 수정)
    public void addCredit(String orderId, long amount, String paymentKey) {
        long id = Long.parseLong(orderId.split("__", 2)[1]);
        Charge charge = chargeRepository.findById(id).orElse(null);

        if (charge == null) {
            throw new RuntimeException("존재하지 않는 주문입니다.");
        }

        Charge _charge = charge.toBuilder()
                .payDate(LocalDateTime.now())
                .paymentKey(paymentKey)
                .build();

        chargeRepository.save(_charge);

        creditLogService.addCreditLog(CreditLog.EventType.충전__토스페이먼츠, amount);
    }


}
