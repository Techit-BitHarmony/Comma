package com.bitharmony.comma.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ncp")
public class NcpConfig {

	private final ImageCredentials imageCredentials;
	private final MusicCredentials musicCredentials;
	private final S3 s3;
	private final ImageOptimizer imageOptimizer;

	@Getter
	@RequiredArgsConstructor
	public static class ImageCredentials {
		private final String accessKey;
		private final String secretKey;
	}

	@Getter
	@RequiredArgsConstructor
	public static class MusicCredentials {
		private final String accessKey;
		private final String secretKey;
	}

	@Getter
	@RequiredArgsConstructor
	public static class S3 {
		private final String region;
		private final String endPoint;
		private final String stack;
		private final String imageBucket;
		private final String musicBucket;
		private final String musicPath;
	}

	@Getter
	@RequiredArgsConstructor
	public static class ImageOptimizer {
		private final String cdn;
		private final String queryString;
	}

}