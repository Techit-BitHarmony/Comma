package com.bitharmony.comma.streaming.entity;

import com.bitharmony.comma.streaming.util.EncodeStatus;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "status", timeToLive = 60*15)
public class Status {

    @Id
    private Long id;

    @Indexed
    private EncodeStatus encodeStatus;

    @Indexed
    private Long albumId;

    @Builder
    public Status(EncodeStatus encodeStatus, Long albumId) {
        this.encodeStatus = encodeStatus;
        this.albumId = albumId;
    }
}
