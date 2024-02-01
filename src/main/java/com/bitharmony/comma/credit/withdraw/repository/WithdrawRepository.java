package com.bitharmony.comma.credit.withdraw.repository;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long>  {
}
