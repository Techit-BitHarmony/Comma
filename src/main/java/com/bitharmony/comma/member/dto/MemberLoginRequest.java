package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberLoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
