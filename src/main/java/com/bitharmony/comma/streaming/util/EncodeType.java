package com.bitharmony.comma.streaming.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EncodeType {

    LOW("64kbps.m4a"),
    MEDIUM("128kbps.m4a"),
    HIGH("256kbps.m4a");

    private final String encodeType;
}
