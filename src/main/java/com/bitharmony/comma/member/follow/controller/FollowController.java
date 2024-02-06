package com.bitharmony.comma.member.follow.controller;

import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.follow.dto.FollowingListReturnResponse;
import com.bitharmony.comma.member.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    //TODO : POST /follow/{username} -> 로그인된 사용자가 해당 username을 가진 사용자를 팔로우
    @PostMapping("/{username}")
    public GlobalResponse follow(@PathVariable("username") String followingUsername) {
        followService.follow(followingUsername);

        return GlobalResponse.of("201");
    }

    //TODO : DELETE /follow/{username} -> 로그인된 사용자가 해당 username을 가진 사용자를 언팔로우
    @DeleteMapping("/{username}")
    public GlobalResponse unfollow(@PathVariable("username") String followingUsername) {
        followService.unfollow(followingUsername);

        return GlobalResponse.of("200");
    }

    //TODO : GET /follow -> 로그인된 사용자가 팔로우한 리스트를 모두 반환
    @GetMapping("/")
    public GlobalResponse getAllFollowing() {
        FollowingListReturnResponse response = followService.getAllFollowingList();
        return GlobalResponse.of("200", response);
    }
}
