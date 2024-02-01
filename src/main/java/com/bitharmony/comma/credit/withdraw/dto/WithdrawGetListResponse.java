package com.bitharmony.comma.credit.withdraw.dto;

import com.bitharmony.comma.credit.withdraw.entity.Withdraw;
import lombok.Builder;

import java.util.List;

@Builder
public record WithdrawGetListResponse(
        List<WithdrawDto> withdrawDtos
) {

}
