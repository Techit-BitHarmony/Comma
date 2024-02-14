package com.bitharmony.comma.credit.withdraw.service;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.repository.WithdrawRepository;
import com.bitharmony.comma.global.exception.credit.HandledWithdrawException;
import com.bitharmony.comma.global.exception.credit.NotEnoughCreditException;
import com.bitharmony.comma.global.exception.credit.WithdrawNotFoundException;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
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
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;
    private final CreditLogService creditLogService;
    private final MemberRepository memberRepository;

    public Withdraw getWithdraw(long id) {
        Optional<Withdraw> withdraw = this.withdrawRepository.findById(id);

        if (withdraw.isEmpty()) {
            throw new WithdrawNotFoundException();
        }

        return withdraw.get();
    }

    public List<Withdraw> getMyWithdrawList(Long id) {
        List<Withdraw> withdraws = withdrawRepository.findByApplicantId(id);

        return withdraws;
    }

    public List<Withdraw> getAllWithdrawList() {
        List<Withdraw> withdraws = this.withdrawRepository.findAll();

        if (withdraws.isEmpty()) {
            throw new WithdrawNotFoundException();
        }

        return withdraws;
    }

    @Transactional
    public Withdraw applyWithdraw(Member member, String bankName, String bankAccountNo, long withdrawAmount) {

        if (!canApply(member, withdrawAmount)) {
            throw new NotEnoughCreditException();
        }

        Withdraw withdraw = Withdraw.builder()
                .applicant(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(withdrawAmount)
                .applyDate(LocalDateTime.now())
                .build();

        withdrawCredit(member, CreditLog.EventType.출금신청__통장입금, withdrawAmount);

        withdrawRepository.save(withdraw);
        return withdraw;
    }

    @Transactional
    public Withdraw modifyWithdraw(Withdraw withdraw, String bankName, String bankAccountNo, long newWithdrawAmount) {

        if (withdraw.isHandled()) {
            throw new HandledWithdrawException();
        }

        checkCreditDifference(withdraw, newWithdrawAmount);

        Withdraw _withdraw = withdraw.toBuilder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(newWithdrawAmount)
                .build();

        this.withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    @Transactional
    public void cancelWithdraw(Member member, Withdraw withdraw) {

        if(withdraw.isHandled()){
            throw new HandledWithdrawException();
        }

        if(!withdraw.getApplicant().equals(member)){
            throw new NotAuthorizedException();
        }

        rebateCredit(member, CreditLog.EventType.출금신청__취소,withdraw.getWithdrawAmount());
        withdrawRepository.deleteById(withdraw.getId());
    }

    // 관리자가 출금 실행하는 메서드
    @Transactional
    public Withdraw doWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (withdraw.isHandled()) {
            throw new HandledWithdrawException();
        }

        Withdraw _withdraw = withdraw.toBuilder()
                .processResult("처리완료")
                .withdrawDoneDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    // 관리자가 출금 거절하는 메서드
    @Transactional
    public Withdraw rejectWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (withdraw.isHandled()) {
            throw new HandledWithdrawException();
        }

        Withdraw _withdraw = withdraw.toBuilder()
                .processResult("거절 - 계좌정보가 불일치")
                .withdrawCancelDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(_withdraw);
        rebateCredit(withdraw.getApplicant(), CreditLog.EventType.출금신청__취소, withdraw.getWithdrawAmount());

        return _withdraw;
    }

    // 사용자의 Credit이 출금 신청액보다 많은지 체크
    public boolean canApply(Member applicant, long withdrawAmount) {
        return applicant.getCredit() >= withdrawAmount;
    }

    // 출금 신청을 수정하는 경우 수정된 금액과 기존 금액을 비교하여 환불, 추가 출금 하는 메서드
    @Transactional
    public void checkCreditDifference(Withdraw withdraw, long newWithdrawAmount) {

        if (newWithdrawAmount > withdraw.getWithdrawAmount()) {
            long creditNeeded = newWithdrawAmount - withdraw.getWithdrawAmount();

            if(!canApply(withdraw.getApplicant(), creditNeeded)){
                throw new NotEnoughCreditException();
            }

            withdrawCredit(withdraw.getApplicant(), CreditLog.EventType.출금신청__수정, creditNeeded);
        }

        if (newWithdrawAmount < withdraw.getWithdrawAmount()){
            long creditRebate = withdraw.getWithdrawAmount() - newWithdrawAmount;
            rebateCredit(withdraw.getApplicant(), CreditLog.EventType.출금신청__수정, creditRebate);
        }
    }

    // 출금 신청시 Credit을 빼는 메서드
    @Transactional
    public void withdrawCredit(Member member, CreditLog.EventType eventType, long amount) {
        Member _member = member.toBuilder()
                .credit(member.getCredit() - amount)
                .build();

        memberRepository.save(_member);
        creditLogService.addCreditLog(member, eventType, -1 * amount);
    }

    // 출금 신청 수정, 출금 거절시 Credit을 돌려주는 메서드
    @Transactional
    public void rebateCredit(Member member, CreditLog.EventType eventType, long amount) {
        Member _member = member.toBuilder()
                .credit(member.getCredit() + amount)
                .build();

        memberRepository.save(_member);
        creditLogService.addCreditLog(member, eventType, amount);

    }

}
