package com.bitharmony.comma.member.service;

import com.bitharmony.comma.global.exception.MemberDuplicateException;
import com.bitharmony.comma.global.exception.MemberNotFoundException;
import com.bitharmony.comma.global.security.SecurityUser;
import com.bitharmony.comma.global.util.JwtUtil;
import com.bitharmony.comma.member.dto.JwtCreateRequest;
import com.bitharmony.comma.member.dto.MemberLoginResponse;
import com.bitharmony.comma.member.dto.MemberModifyRequest;
import com.bitharmony.comma.member.dto.MemberReturnResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public MemberLoginResponse login(String username, String password) {
        Member member = getMemberByUsername(username);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("정확한 비밀번호를 입력해주세요");
        }

        JwtCreateRequest jwtCreateRequest = JwtCreateRequest.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();

        String accessToken = jwtUtil.createAccessToken(jwtCreateRequest);
        String refreshToken = jwtUtil.createRefreshToken(jwtCreateRequest);

        MemberLoginResponse response = MemberLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return response;
    }

    @Transactional
    public void logout() {
        SecurityUser loginedMember = getUser();
        if (redisTemplate.opsForValue().get(loginedMember.getUsername()) != null) {
            redisTemplate.delete(loginedMember.getUsername());
        }
    }

    @Transactional
    public void join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new MemberDuplicateException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new MemberDuplicateException("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);
    }

    public Member getMemberByUsername(String username) {

        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        return findMember;
    }

    public MemberReturnResponse getProfile() {

        SecurityUser loginedMember = getUser();
        Member findMember = getMemberByUsername(loginedMember.getUsername());

        MemberReturnResponse response = MemberReturnResponse.builder()
                .username(findMember.getUsername())
                .Email(findMember.getEmail())
                .nickname(findMember.getNickname())
                .build();

        return response;
    }

    @Transactional
    public void modify(String nickname, String email) {

        SecurityUser user = getUser();
        long id = user.getId();
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        Member modifyMember = findMember.toBuilder()
                .nickname(nickname)
                .email(email)
                .build();

        memberRepository.save(modifyMember);
    }

    private SecurityUser getUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
