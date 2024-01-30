package com.bitharmony.comma.album.file.util;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.bitharmony.comma.global.config.NcpConfig;

import lombok.Getter;

@Getter
@Component
public class NcpImageUtil {
	private final AmazonS3 amazonS3;

	public final String bucketName;

	public NcpImageUtil(NcpConfig ncpConfig) {
		bucketName = ncpConfig.getS3().getImageBucket();

		String accessKey = ncpConfig.getImageCredentials().getAccessKey();
		String secretKey = ncpConfig.getImageCredentials().getSecretKey();
		String endPoint = ncpConfig.getS3().getEndPoint();
		String region = ncpConfig.getS3().getRegion();

		amazonS3 = AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			.build();
	}
}