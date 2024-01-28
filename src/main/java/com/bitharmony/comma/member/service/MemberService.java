package com.bitharmony.comma.member.service;

import com.bitharmony.comma.global.exception.MemberDuplicateException;
import com.bitharmony.comma.global.exception.MemberNotFoundException;
import com.bitharmony.comma.global.util.JwtUtil;
import com.bitharmony.comma.member.dto.JwtCreateRequest;
import com.bitharmony.comma.member.dto.MemberJoinResponse;
import com.bitharmony.comma.member.dto.MemberLoginResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberLoginResponse login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("정확한 비밀번호를 입력해주세요");
        }

        JwtCreateRequest jwtCreateRequest = JwtCreateRequest.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();

        String token = JwtUtil.createToken(jwtCreateRequest);

        MemberLoginResponse response = MemberLoginResponse.builder()
                .accessToken(token)
                .build();

        return response;
    }

    @Transactional
    public MemberJoinResponse join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new MemberDuplicateException("이미 존재하는 회원입니다");
        }
        // TODO : Email or Nickname도 유니크하게 유지할 것인지 물어보고, 한다고하면 중복체크 로직을 추가

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        MemberJoinResponse response = MemberJoinResponse.builder()
                .message("회원가입이 완료되었습니다.")
                .build();

        return response;
    }
}
