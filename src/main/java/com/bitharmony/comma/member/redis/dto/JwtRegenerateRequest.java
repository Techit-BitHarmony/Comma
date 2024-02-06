package com.bitharmony.comma.member.redis.dto;

import lombok.Builder;

@Builder
public record JwtRegenerateRequest(
        String refreshToken
) {
}
