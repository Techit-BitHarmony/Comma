package com.bitharmony.comma.member.follow.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record FollowingListReturnResponse(
        List<String> followingList
) {
}
