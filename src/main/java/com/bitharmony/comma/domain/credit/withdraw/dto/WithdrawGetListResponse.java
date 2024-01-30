package com.bitharmony.comma.domain.credit.withdraw.dto;

import com.bitharmony.comma.domain.credit.withdraw.entity.Withdraw;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record WithdrawGetListResponse(
        List<WithdrawDto> withdrawDtos
) {
    public static WithdrawGetListResponse toDtoList(List<Withdraw> withdraws){
        List<WithdrawDto> withdrawDtos = withdraws.stream().map(WithdrawDto::new).toList();

        return new WithdrawGetListResponse(withdrawDtos);
    }
}
