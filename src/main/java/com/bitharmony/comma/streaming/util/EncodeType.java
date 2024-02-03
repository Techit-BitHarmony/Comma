package com.bitharmony.comma.streaming.util;

import lombok.Getter;

@Getter
public enum EncodeType {
    LOW("64kbps.m4a"),
    MEDIUM("128kbps.m4a"),
    HIGH("160kbps.m4a"),
    VERY_HIGH("256kbps.m4a");

    EncodeType(String filename) {
        this.filename = filename;
    }

    private final String filename;
}
