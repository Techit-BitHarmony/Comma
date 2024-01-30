package com.bitharmony.comma.domain.credit.creditLog.repository;

import com.bitharmony.comma.domain.credit.creditLog.entity.CreditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditLogRepository extends JpaRepository<CreditLog, Long> {
}
