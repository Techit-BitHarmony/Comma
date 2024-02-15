package com.bitharmony.comma.credit.withdraw.repository;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long>  {
    Page<Withdraw> findByApplicantId(Long id, Pageable pageable);
    Page<Withdraw> findAll(Pageable pageable);
}
