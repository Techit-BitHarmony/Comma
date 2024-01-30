package com.bitharmony.comma.global.security;

import com.bitharmony.comma.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("accessToken = {}", accessToken);

        if (accessToken != null && jwtUtil.validToken(accessToken)) {
            Map<String, Object> userData = jwtUtil.getUserData(accessToken);
            long id = Long.parseLong((String) userData.get("id"));
            String username = (String) userData.get("username");

            Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            // Jwt 인증을 성공한 객체에 임의로 USER 역할을 부여하여, 인가를 허용.

            setAuthentication(new SecurityUser(id, username, "", authorities));
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
