package com.bitharmony.comma.member.follow.service;

import com.bitharmony.comma.global.exception.MemberDuplicateException;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.follow.dto.FollowingListReturnResponse;
import com.bitharmony.comma.member.follow.entity.Follow;
import com.bitharmony.comma.member.follow.repository.FollowRepository;
import com.bitharmony.comma.member.service.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberService memberService;


    @Transactional
    public void follow(String followingUser) {
        String username = memberService.getUser().getUsername();
        if (username.equals(followingUser)) {
            throw new MemberDuplicateException("본인을 팔로우 할 수 없습니다.");
        }

        Member follower = memberService.getMemberByUsername(username);
        Member following = memberService.getMemberByUsername(followingUser);

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
        Member following = memberService.getMemberByUsername(followingUser);
        String username = memberService.getUser().getUsername();
        Member follower = memberService.getMemberByUsername(username);

        Follow follow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId())
                .orElseThrow(() -> new RuntimeException("팔로우 정보가 없습니다."));

        followRepository.delete(follow);
    }

    public FollowingListReturnResponse getAllFollowingList() {
        String username = memberService.getUser().getUsername();
        Member findMember = memberService.getMemberByUsername(username);

        List<String> followingList = findMember.getFollowingList().stream()
                .map((follow) -> follow.getFollowing().getUsername()).toList();

        FollowingListReturnResponse response = FollowingListReturnResponse.builder()
                .followingList(followingList)
                .build();

        return response;
    }
}
