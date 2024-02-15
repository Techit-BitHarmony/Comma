package com.bitharmony.comma.credit.withdraw.dto;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record WithdrawGetListResponse(
        Page<Withdraw> withdraws
) {

}
