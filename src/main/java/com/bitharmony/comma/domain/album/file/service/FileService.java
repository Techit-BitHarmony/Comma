package com.bitharmony.comma.domain.album.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bitharmony.comma.domain.album.file.dto.FileResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final AmazonS3 amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public String getUuidFileName(String fileName) {
		String ext = fileName.substring(fileName.indexOf(".") + 1);
		return UUID.randomUUID().toString() + "." + ext;
	}

	//NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
	public FileResponse uploadFile(MultipartFile multipartFile, String filePath) {
		String originalFileName = multipartFile.getOriginalFilename();
		String uploadFileName = getUuidFileName(originalFileName);
		String uploadFileUrl = "";

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			String keyName = filePath + "/" + uploadFileName;

			// S3에 폴더 및 파일 업로드
			amazonS3Client.putObject(
				new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata).withCannedAcl(
					CannedAccessControlList.PublicRead));

			// S3에 업로드한 폴더 및 파일 URL
			uploadFileUrl = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return FileResponse.builder()
			.originalFileName(originalFileName)
			.uploadFileName(uploadFileName)
			.uploadFilePath(filePath)
			.uploadFileUrl(uploadFileUrl)
			.build();
	}
}