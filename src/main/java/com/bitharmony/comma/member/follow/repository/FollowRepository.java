package com.bitharmony.comma.member.follow.repository;

import com.bitharmony.comma.member.follow.entity.Follow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
