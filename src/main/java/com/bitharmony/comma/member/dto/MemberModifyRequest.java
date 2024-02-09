package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberModifyRequest(
        @NotBlank(message = "Nickname을 입력해주세요.") String nickname,
        @NotBlank(message = "Email을 입력해주세요.") @Email String email
) {
}
