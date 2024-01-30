package com.bitharmony.comma.domain.credit.charge.repository;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
}
