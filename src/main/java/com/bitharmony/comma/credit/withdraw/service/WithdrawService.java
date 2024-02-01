package com.bitharmony.comma.credit.withdraw.service;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.repository.WithdrawRepository;
import com.bitharmony.comma.global.exception.HandledWithdrawException;
import com.bitharmony.comma.global.exception.NotEnoughCreditException;
import com.bitharmony.comma.global.exception.WithdrawNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

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

        if (canApply(member, withdrawAmount)) {
            throw new NotEnoughCreditException();
        }

        Withdraw withdraw = Withdraw.builder()
                .applicant(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(withdrawAmount)
                .applyDate(LocalDateTime.now())
                .build();

        this.withdrawRepository.save(withdraw);
        return withdraw;
    }

    public Withdraw modifyWithdraw(long id, String bankName, String bankAccountNo, long withdrawAmount) {
        Optional<Withdraw> withdraw = this.withdrawRepository.findById(id);

        if (withdraw.isEmpty()) {
            throw new WithdrawNotFoundException();
        }

        Withdraw _withdraw = withdraw.get().toBuilder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(withdrawAmount)
                .build();

        this.withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public void delete(long withdrawId) {
        withdrawRepository.deleteById(withdrawId);
    }

    public Withdraw doWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (!canDoOrCancel(withdraw)) {
            throw new HandledWithdrawException();
        }

        Withdraw _withdraw = withdraw.toBuilder()
                .processResult("처리완료")
                .withdrawCancelDate(LocalDateTime.now())
                .build();

        withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public Withdraw cancelWithdraw(long id) {
        Withdraw withdraw = getWithdraw(id);

        if (!canDoOrCancel(withdraw)) {
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

    //    TODO : 멤버 찾기 메서드 추가시 수정할 것
    public boolean canDelete(Member member, Withdraw withdraw) {
        if (withdraw.getWithdrawDoneDate() != null) {
            return false;
        }
        if (withdraw.getWithdrawCancelDate() != null) {
            return false;
        }

//        if (member.isAdmin()) return true;

        if (!withdraw.getApplicant().equals(member)) {
            return false;
        }

        return true;
    }


    public boolean canDoOrCancel(Withdraw withdraw) {

        if (withdraw.getWithdrawCancelDate() != null) {
            return false;
        }
        if (withdraw.getWithdrawDoneDate() != null) {
            return false;
        }

        return true;
    }
}
