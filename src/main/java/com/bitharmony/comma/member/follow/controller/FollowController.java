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

    @PostMapping("/{username}")
    public GlobalResponse follow(@PathVariable("username") String followingUsername) {
        followService.follow(followingUsername);

        return GlobalResponse.of("201");
    }

    @DeleteMapping("/{username}")
    public GlobalResponse unfollow(@PathVariable("username") String followingUsername) {
        followService.unfollow(followingUsername);

        return GlobalResponse.of("200");
    }

    @GetMapping
    public GlobalResponse getAllFollowing() {
        FollowingListReturnResponse response = followService.getAllFollowingList();
        return GlobalResponse.of("200", response);
    }
}
