package com.bitharmony.comma.streaming.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.bitharmony.comma.global.config.NcpConfig;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NcpMusicUtil {
    private final AmazonS3 amazonS3;

    public final String bucketName;
    public final String path;

    public NcpMusicUtil(NcpConfig ncpConfig) {
        bucketName = ncpConfig.getS3().getMusicBucket();
        path = ncpConfig.getS3().getMusicPath();

        String accessKey = ncpConfig.getMusicCredentials().getAccessKey();
        String secretKey = ncpConfig.getMusicCredentials().getSecretKey();
        String endPoint = ncpConfig.getS3().getEndPoint();
        String region = ncpConfig.getS3().getRegion();

        amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public URL generatePresignedUrl(String name) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

        Date from = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        return amazonS3.generatePresignedUrl(bucketName, path + name, from, HttpMethod.PUT);
    }

}
