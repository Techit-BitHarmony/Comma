package com.bitharmony.comma.credit.creditLog.repository;

import com.bitharmony.comma.credit.creditLog.entity.CreditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditLogRepository extends JpaRepository<CreditLog, Long> {
    Page<CreditLog> findByMemberId(Long id, Pageable pageable);
}
