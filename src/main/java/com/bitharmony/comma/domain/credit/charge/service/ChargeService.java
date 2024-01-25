package com.bitharmony.comma.domain.credit.charge.service;

import com.bitharmony.comma.domain.credit.charge.entity.Charge;
import com.bitharmony.comma.domain.credit.charge.repository.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;


    public Charge getChargeById(long id) {
        Optional<Charge> charge = this.chargeRepository.findById(id);

        if(charge.isEmpty()){
            throw new RuntimeException("존재하지 않는 주문입니다.");
        }

        return charge.get();
    }

}
