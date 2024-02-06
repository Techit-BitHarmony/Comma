package com.bitharmony.comma.member.follow.service;

import com.bitharmony.comma.global.exception.MemberDuplicateException;
import com.bitharmony.comma.global.exception.MemberNotFoundException;
import com.bitharmony.comma.global.security.SecurityUser;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.follow.dto.FollowingListReturnResponse;
import com.bitharmony.comma.member.follow.entity.Follow;
import com.bitharmony.comma.member.follow.repository.FollowRepository;
import com.bitharmony.comma.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void follow(String followingUser) {
        String username = getUser().getUsername();
        if (username.equals(followingUser)) {
            throw new MemberDuplicateException("본인을 팔로우 할 수 없습니다.");
        }

        Member follower = getMemberByUsername(username);
        Member following = getMemberByUsername(followingUser);

        Optional<Follow> isExist = followRepository.findByFollowerIdAndFollowingId(follower.getId(),
                following.getId());

        if (isExist.isPresent()) {
            throw new RuntimeException("이미 팔로우한 상태입니다.");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String followingUser) {
        Member following = getMemberByUsername(followingUser);
        Member follower = getMemberByUsername(getUser().getUsername());

        Follow follow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId())
                .orElseThrow(() -> new RuntimeException("팔로우 정보가 없습니다."));

        followRepository.delete(follow);
    }

    public FollowingListReturnResponse getAllFollowingList() {
        Member findMember = getMemberByUsername(getUser().getUsername());
        List<String> followingList = findMember.getFollowingList().stream()
                .map((follow) -> follow.getFollowing().getUsername()).toList();

        FollowingListReturnResponse response = FollowingListReturnResponse.builder()
                .followingList(followingList)
                .build();

        return response;
    }

    private SecurityUser getUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Member getMemberByUsername(String username) {

        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        return findMember;
    }
}
