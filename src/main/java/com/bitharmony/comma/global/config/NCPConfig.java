package com.bitharmony.comma.global.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ncp")
public class NCPConfig {

    private final NCPS3Config ncps3Config;
    private final NCPStreamingConfig ncpStreamingConfig;

    @Getter
    @RequiredArgsConstructor
    public static class NCPS3Config {
        private final String accessKey;
        private final String secretKey;
        private final String regionName;
        private final String bucketName;
        private final String endPoint;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NCPStreamingConfig {
        private final String path;
    }

}
