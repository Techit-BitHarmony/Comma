package com.bitharmony.comma.member.redis.service;

import com.bitharmony.comma.global.util.JwtUtil;
import com.bitharmony.comma.member.dto.JwtCreateRequest;
import com.bitharmony.comma.member.dto.MemberLoginResponse;
import com.bitharmony.comma.member.redis.dto.JwtRegenerateRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${custom.jwt.token.refresh-expiration-time}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    @Transactional
    public MemberLoginResponse reissue(JwtRegenerateRequest jwtRegenerateRequest) {

        String refreshToken = jwtRegenerateRequest.refreshToken();

        // Refresh Token 검증
        if (!jwtUtil.validToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token supplied");
        }

        // Access Token 에서 User name을 가져온다.
        Map<String, String> userData = jwtUtil.getUserData(refreshToken);

        // Redis에서 저장된 Refresh Token 값을 가져온다.
        String getRefreshToken = redisTemplate.opsForValue().get(userData.get("username"));
        if (!getRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh Token doesn't match.");
        }

        JwtCreateRequest jwtCreateRequest = JwtCreateRequest.builder()
                .id(Long.parseLong(userData.get("id")))
                .username((String) userData.get("username"))
                .build();

        // 토큰 재발행
        String newRefreshToken = jwtUtil.createRefreshToken(jwtCreateRequest);

        MemberLoginResponse response = MemberLoginResponse.builder()
                .accessToken(jwtUtil.createAccessToken(jwtCreateRequest))
                .refreshToken(newRefreshToken)
                .build();

        return response;
    }
}
