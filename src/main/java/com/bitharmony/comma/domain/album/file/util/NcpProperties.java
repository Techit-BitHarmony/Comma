package com.bitharmony.comma.domain.album.file.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class NcpProperties {
	@Value("${cloud.aws.s3.image-bucket}")
	private String imageBucketName;

	@Value("${cloud.aws.s3.music-bucket}")
	private String musicBucketName;

	@Value("${ncp.object-storage.img-folder}")
	private String uploadFolder;

	@Value("${ncp.image-optimizer.cdn}")
	private String cdnUrl;

	@Value("${ncp.image-optimizer.query-string}")
	private String cdnQueryString;
}
