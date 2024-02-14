package com.bitharmony.comma.streaming.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.bitharmony.comma.global.config.NcpConfig;
import com.bitharmony.comma.global.exception.streaming.MusicFileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public URL generatePresignedUrl(String filePath) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(15);

        Date from = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        return amazonS3.generatePresignedUrl(bucketName, filePath, from, HttpMethod.PUT);
    }

    public void deleteFile(String inputFilePath) {
        List<String> filePathList = getFilePathList(inputFilePath);

        for (String filePath : filePathList) {
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, filePath));
            } catch (SdkClientException e) {
                throw new MusicFileNotFoundException();
            }
        }
    }

    private List<String> getFilePathList(String inputFilePath) {
        List<String> filePathList = new ArrayList<>();
        filePathList.add(inputFilePath);

        String outputFilePath = inputFilePath.replace("input", "output");
        filePathList.addAll(generateEncodedFileNames(outputFilePath));

        return filePathList;
    }

    private List<String> generateEncodedFileNames(String outputFilePath) {
        List<String> encodedFileNames = new ArrayList<>();
        int dotIndex = outputFilePath.lastIndexOf(".");

        for (EncodeType encodeType : EncodeType.values()) {
            String newFilename = outputFilePath.substring(0, dotIndex) + "_" + encodeType.getEncodeType();
            encodedFileNames.add(newFilename);
        }

        return encodedFileNames;
    }

}
