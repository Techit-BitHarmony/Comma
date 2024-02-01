package com.bitharmony.comma.credit.withdraw.service;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import com.bitharmony.comma.credit.withdraw.repository.WithdrawRepository;
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


    // 추후 멤버 기능 추가시 멤버 아이디로 리스트 가져오는 것으로 변경 예정
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

    public void delete(long withdrawId) {
        try {
            withdrawRepository.deleteById(withdrawId);
        } catch(Exception e) {
            throw new RuntimeException("존재하지 않는 출금 신청입니다.");
        }
    }

//    멤버 기능 연동 후 수정 예정
//    public boolean canDelete(Member member, Withdraw withdraw){
//        if (withdraw.getWithdrawDoneDate() != null) {
//            return false;
//        }
//        if (withdraw.getWithdrawCancelDate() != null) {
//            return false;
//        }
//
//        if (member.isAdmin()) return true;
//
//        if (!withdraw.getApplicant().equals(member)) {
//            return false;
//        }
//
//        return true;
//    }
}
