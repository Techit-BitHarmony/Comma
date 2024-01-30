package com.bitharmony.comma.domain.credit.withdraw.repository;

import com.bitharmony.comma.domain.credit.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long>  {
}
