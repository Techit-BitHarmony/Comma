package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberPwModifyRequest(
        @NotNull String passwordModify,
        @NotNull String passwordModifyCheck
) {
}
