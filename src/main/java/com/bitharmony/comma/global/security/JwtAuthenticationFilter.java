package com.bitharmony.comma.global.security;

import com.bitharmony.comma.global.exception.ExpiredAccessTokenException;
import com.bitharmony.comma.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (accessToken != null && jwtUtil.validToken(accessToken)) {
                log.info("accessToken in JwtAuthenticationFilter = {}", accessToken);
                Map<String, String> userData = jwtUtil.getUserData(accessToken);
                long id = Long.parseLong(userData.get("id"));
                String username = userData.get("username");

                Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_USER"));
                // Jwt 인증을 성공한 객체에 임의로 USER 역할을 부여하여, 인가를 허용.

                // 로그인 여부 체크, access token은 넘어오지만 refresh token이 redis에 저장이 되어 있지 않기 때문에 로그아웃된 사용자라는 뜻이다
                String refreshToken = redisTemplate.opsForValue().get(username);
                if (redisTemplate.hasKey(username) && refreshToken != null) {
                    setAuthentication(new SecurityUser(id, username, "", authorities));
                }
            }
        } catch (ExpiredAccessTokenException ex) {
            request.setAttribute("exception", ex);
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(SecurityUser user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}