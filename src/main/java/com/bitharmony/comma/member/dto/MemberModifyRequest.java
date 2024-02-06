package com.bitharmony.comma.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberModifyRequest(
        @NotBlank String nickname,
        @NotBlank @Email String email
) {
}
