package com.bitharmony.comma.credit.charge.service;

import com.bitharmony.comma.credit.charge.dto.ChargeConfirmResponse;
import com.bitharmony.comma.credit.charge.entity.Charge;
import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.credit.charge.repository.ChargeRepository;
import com.bitharmony.comma.global.exception.ChargeAmountNotMatchException;
import com.bitharmony.comma.global.exception.ChargeNotFoundException;
import com.bitharmony.comma.global.exception.PaymentFailureException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final CreditLogService creditLogService;
    private final MemberRepository memberRepository;
    private final TossPaymentsService tossPaymentsService;


    public Charge getChargeById(long id) {
        Optional<Charge> charge = this.chargeRepository.findById(id);

        if (charge.isEmpty()) {
            throw new ChargeNotFoundException();
        }

        return charge.get();
    }

    public List<Charge> getChargeList() {
        return this.chargeRepository.findAll();
    }

    public List<Charge> getChargeListByMemberId(Long id) {
        return chargeRepository.findByChargerId(id);
    }


    @Transactional
    public Charge createCharge(Member member, long chargeAmount) {
        Charge charge = Charge.builder()
                .charger(member)
                .chargeAmount(chargeAmount)
                .build();
        this.chargeRepository.save(charge);

        return charge;
    }


    // 주문서(Charge)와 결제요청의 정보가 일치하는지 확인하는 메서드
    public void checkValidity(String orderId, long amount) {
        long id = Long.parseLong(orderId.split("__", 2)[1]);
        Charge charge = chargeRepository.findById(id).orElse(null);

        if (charge == null) {
            throw new ChargeNotFoundException();
        }

        if (charge.getChargeAmount() != amount) {
            throw new ChargeAmountNotMatchException();
        }
    }


    // 결제 성공시 멤버의 크레딧을 증가시키고 크레딧 로그를 남기는 메서드 (추후 멤버 기능 연동시 수정)
    @Transactional
    public void approvePayment(String orderId, long amount, String paymentKey) {
        long id = Long.parseLong(orderId.split("__", 2)[1]);
        Charge charge = chargeRepository.findById(id).orElse(null);

        if (charge == null) {
            throw new ChargeNotFoundException();
        }

        Charge _charge = charge.toBuilder()
                .payDate(LocalDateTime.now())
                .paymentKey(paymentKey)
                .build();

        chargeRepository.save(_charge);
        addCredit(_charge.getCharger(), amount);

    }


    public ChargeConfirmResponse confirmPayment(String orderId, String amount, String paymentKey) {

        // orderId와 amount 맞는지 체크하는 메서드
        checkValidity(orderId, Long.parseLong(amount));

        try {
            ChargeConfirmResponse chargeConfirmResponse =
                    tossPaymentsService.requestApprovalAndGetResponse(orderId, amount, paymentKey);

            if(chargeConfirmResponse.isApproved()) {
                approvePayment(orderId, Long.parseLong(amount), paymentKey);
            }

            return chargeConfirmResponse;
        } catch (Exception e) {
            throw new PaymentFailureException();
        }
    }

    public void addCredit(Member member, long amount){
        Member _member = member.toBuilder()
                .credit(member.getCredit() + amount)
                .build();

        memberRepository.save(member);
        creditLogService.addCreditLog(_member, CreditLog.EventType.충전__토스페이먼츠, amount);
    }
}
