package com.bitharmony.comma.global.config;

import com.bitharmony.comma.global.exception.CustomAccessDeniedHandled;
import com.bitharmony.comma.global.exception.CustomAuthenticationEntryPoint;
import com.bitharmony.comma.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandled accessDeniedHandled;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // csrf 토큰 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // form을 통한 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // security 기본 인증 사용 비활성화
                .sessionManagement(session -> { // 세션을 사용하지 않기 때문에 세션 관리를 stateless로 설정
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(exception -> // Exception part는 다시 공부하기
                {
                    exception.authenticationEntryPoint(authenticationEntryPoint); // 인증 에러
                    exception.accessDeniedHandler(accessDeniedHandled); // 인가 에러
                });


        http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/member/login", "/member/join","/member/{username}" ,"/reissue",
                            "/album/list", "/album/{albumId}", "/album/{username}", "/album/detail/{id}",
                            "/streaming/status", "/community/articles/user/{username}", "/community/comments").permitAll();
                    auth.anyRequest().authenticated(); // 그 외의 것들은 모두 인증, 인가를 거치도록 설정
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // username & password로 검증하기 전에 Jwt를 통한 인증, 인가 설정

        http.cors(
                cors -> cors.configure(http)
        );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}