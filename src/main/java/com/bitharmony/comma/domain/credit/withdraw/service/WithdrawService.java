package com.bitharmony.comma.domain.credit.withdraw.service;

import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import com.bitharmony.comma.domain.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.domain.credit.withdraw.repository.WithdrawRepository;
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
            throw new RuntimeException("존재하지 않는 신청입니다.");
        }

        return withdraw.get();
    }

    public List<Withdraw> getWithdrawList() {
        return this.withdrawRepository.findAll();
    }

    public Withdraw applyWithdraw(String bankName, String bankAccountNo, long withdrawAmount) {
        Withdraw withdraw = new Withdraw().builder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(withdrawAmount)
                .applyDate(LocalDateTime.now())
                .build();

        this.withdrawRepository.save(withdraw);
        return withdraw;
    }

    public Withdraw modifyWithdraw(long id, String bankName, String bankAccountNo, long withdrawAmount){
        Optional<Withdraw> withdraw = this.withdrawRepository.findById(id);

        if(withdraw.isEmpty()){
            throw new RuntimeException("존재하지 않는 신청입니다.");
        }

        Withdraw _withdraw = withdraw.get().toBuilder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .withdrawAmount(withdrawAmount)
                .build();

        this.withdrawRepository.save(_withdraw);

        return _withdraw;
    }

    public void cancelWithdraw(long id){
        this.withdrawRepository.deleteById(id);
    }

}
