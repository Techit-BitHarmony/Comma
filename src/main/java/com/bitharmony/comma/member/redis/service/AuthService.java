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

    @Transactional
    public MemberLoginResponse reissue(JwtRegenerateRequest jwtRegenerateRequest) {

        String refreshToken = jwtRegenerateRequest.refreshToken();

        if (!jwtUtil.validToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token supplied");
        }

        Map<String, String> userData = jwtUtil.getUserData(refreshToken);

        String getRefreshToken = redisTemplate.opsForValue().get(userData.get("username"));
        if (!getRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh Token doesn't match.");
        }

        JwtCreateRequest jwtCreateRequest = JwtCreateRequest.builder()
                .id(Long.parseLong(userData.get("id")))
                .username((String) userData.get("username"))
                .build();

        String newRefreshToken = jwtUtil.createRefreshToken(jwtCreateRequest);

        MemberLoginResponse response = MemberLoginResponse.builder()
                .accessToken(jwtUtil.createAccessToken(jwtCreateRequest))
                .refreshToken(newRefreshToken)
                .build();

        return response;
    }
}
