package com.bitharmony.comma.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberJoinRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String passwordCheck,
        @NotBlank @Email String email,
        @NotBlank String nickname
) { }
