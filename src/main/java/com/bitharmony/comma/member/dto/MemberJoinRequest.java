package com.bitharmony.comma.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberJoinRequest(
        @NotBlank(message = "ID를 입력해주세요.") String username,
        @NotBlank(message = "Password를 입력해주세요.") String password,
        @NotBlank(message = "Password 확인을 입력해주세요.") String passwordCheck,
        @NotBlank(message = "Email을 입력해주세요.") @Email(message = "올바른 email 형식을 입력해주세요.") String email,
        @NotBlank(message = "Nickname을 입력해주세요.") String nickname
) { }
