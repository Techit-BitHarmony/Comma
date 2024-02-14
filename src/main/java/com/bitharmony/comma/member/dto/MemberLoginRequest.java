package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberLoginRequest(
        @NotBlank(message = "ID를 입력해주세요.") String username,
        @NotBlank(message = "Password를 입력해주세요.") String password) {
}
