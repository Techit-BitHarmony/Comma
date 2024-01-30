package com.bitharmony.comma.member.dto;

import lombok.Builder;

@Builder
public record MemberLoginResponse (
        String accessToken,
        String refreshToken
){
}
