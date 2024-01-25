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

	@Value("${ncp.image-optimizer.cdn}")
	private String cdnUrl;

	@Value("${ncp.image-optimizer.query-string}")
	private String cdnQueryString;

	public String getUuidFileName(String fileName) {
		String ext = fileName.substring(fileName.indexOf(".") + 1);
		return UUID.randomUUID().toString() + "." + ext;
	}

	/**
	 *NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
	 *ncp object storage에 파일 업로드
	 */
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
			uploadFileUrl = bucketName + "/" + keyName;

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

	/**
	 * ncp image optimizer 사용 cdn url을 통해서 변환된 이미지 가져오기
	 */
	public String getAlbumImageUrl(String filepath) {
		String replacedFilePath = filepath.replace(bucketName,"");

		//이미지 URL검증 필요시
		// try {
		// 	// 이미지 URL을 검증하는 코드를 추가합니다.
		// 	// 이 부분은 실제 이미지 URL을 검증하는 로직에 따라 달라집니다.
		// 	validateImageUrl(optimizedImageUrl);
		// } catch (FileNotFoundException e) {
		// 	// 적절한 에러 메시지를 설정하거나 다른 조치를 취합니다.
		// 	System.out.println("The requested image does not exist: " + optimizedImageUrl);
		// 	return null; // 또는 적절한 에러 응답을 반환합니다.
		// }

		return cdnUrl + replacedFilePath + cdnQueryString;
	}
}