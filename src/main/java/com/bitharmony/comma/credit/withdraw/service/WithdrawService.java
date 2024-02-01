package com.bitharmony.comma.credit.withdraw.service;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.credit.creditLog.service.CreditLogService;
import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.repository.WithdrawRepository;
import com.bitharmony.comma.global.exception.HandledWithdrawException;
import com.bitharmony.comma.global.exception.NotEnoughCreditException;
import com.bitharmony.comma.global.exception.WithdrawNotFoundException;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import io.lettuce.core.AbstractRedisAsyncCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        if (withdraws.isEmpty()) {
            throw new WithdrawNotFoundException();
        }
        return withdraws;
    }


    public List<Withdraw> getAllWithdrawList() {
        List<Withdraw> withdraws = this.withdrawRepository.findAll();

        if (withdraws.isEmpty()) {
            throw new WithdrawNotFoundException();
        }

        return withdraws;
    }

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

    public Withdraw modifyWithdraw(Withdraw withdraw, String bankName, String bankAccountNo, long newWithdrawAmount) {

        if (isHandled(withdraw)) {
            throw new HandledWithdrawException();
        }

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

        Withdraw _withdraw = withdraw.toBuilder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(newWithdrawAmount)
                .build();

        this.withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public void delete(Member member, Withdraw withdraw) {

        if(isHandled(withdraw)){
            throw new HandledWithdrawException();
        }

        if(!withdraw.getApplicant().equals(member)){
            throw new NotAuthorizedException();
        }
        rebateCredit(member, CreditLog.EventType.출금신청__취소,withdraw.getWithdrawAmount());
        withdrawRepository.deleteById(withdraw.getId());
    }

    public Withdraw doWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (isHandled(withdraw)) {
            throw new HandledWithdrawException();
        }

        Withdraw _withdraw = withdraw.toBuilder()
                .processResult("처리완료")
                .withdrawDoneDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public Withdraw cancelWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (isHandled(withdraw)) {
            throw new HandledWithdrawException();
        }

        Withdraw _withdraw = withdraw.toBuilder()
                .processResult("취소 - 계좌정보가 불일치")
                .withdrawCancelDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public boolean canApply(Member applicant, long withdrawAmount) {
        return applicant.getCredit() >= withdrawAmount;
    }


    public boolean isHandled(Withdraw withdraw) {

        if (withdraw.getWithdrawCancelDate() != null) {
            return true;
        }
        if (withdraw.getWithdrawDoneDate() != null) {
            return true;
        }

        return false;
    }

    public void withdrawCredit(Member member, CreditLog.EventType eventType, long amount) {
        Member _member = member.toBuilder()
                .credit(member.getCredit() - amount)
                .build();

        memberRepository.save(_member);
        creditLogService.addCreditLog(member, eventType, -1 * amount);
    }

    public void rebateCredit(Member member, CreditLog.EventType eventType, long amount) {
        Member _member = member.toBuilder()
                .credit(member.getCredit() + amount)
                .build();

        memberRepository.save(_member);
        creditLogService.addCreditLog(member, eventType, amount);

    }

}
