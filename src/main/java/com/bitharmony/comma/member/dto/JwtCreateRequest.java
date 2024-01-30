package com.bitharmony.comma.member.dto;

import lombok.Builder;

@Builder
public record JwtCreateRequest (
        long id,
        String username
){
}
