package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberPwModifyRequest(
        @NotBlank(message = "Password을 입력해주세요.") String passwordModify,
        @NotBlank(message = "Password 확인을 입력해주세요.") String passwordModifyCheck
) {
}
