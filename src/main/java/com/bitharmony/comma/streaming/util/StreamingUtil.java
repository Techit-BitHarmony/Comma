package com.bitharmony.comma.streaming.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.bitharmony.comma.global.config.NCPConfig;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class StreamingUtil {
    private final AmazonS3 amazonS3;

    public final String bucketName;
    public final String path;

    public StreamingUtil(NCPConfig ncpConfig) {
        bucketName = ncpConfig.getNcps3Config().getBucketName();
        path = ncpConfig.getNcpStreamingConfig().getPath();

        String accessKey = ncpConfig.getNcps3Config().getAccessKey();
        String secretKey = ncpConfig.getNcps3Config().getSecretKey();
        String endPoint = ncpConfig.getNcps3Config().getEndPoint();
        String regionName = ncpConfig.getNcps3Config().getRegionName();

        amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public URL generatePresignedUrl(String name) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

        Date from = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        return amazonS3.generatePresignedUrl(bucketName, name, from, HttpMethod.PUT);
    }

}
