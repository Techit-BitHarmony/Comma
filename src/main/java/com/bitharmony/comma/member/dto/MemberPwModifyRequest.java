package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberPwModifyRequest(
        @NotNull(message = "Password을 입력해주세요.") String passwordModify,
        @NotNull(message = "Password 확인을 입력해주세요.") String passwordModifyCheck
) {
}
