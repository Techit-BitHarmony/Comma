package com.bitharmony.comma.album.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bitharmony.comma.album.file.dto.FileResponse;
import com.bitharmony.comma.album.file.util.FileType;
import com.bitharmony.comma.album.file.util.NcpImageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	private final NcpImageUtil ncpImageUtil;

	public String getUuidFileName(String fileName) {
		String ext = fileName.substring(fileName.indexOf(".") + 1);
		return UUID.randomUUID() + "." + ext;
	}

	/**
	 *NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
	 *ncp object storage에 파일 업로드
	 */
	public FileResponse uploadFile(MultipartFile multipartFile, String bucketName) {
		String originalFileName = multipartFile.getOriginalFilename();
		String uploadFileName = getUuidFileName(originalFileName);
		String uploadFileUrl = "";

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			String keyName = uploadFileName;

			// S3에 폴더 및 파일 업로드
			ncpImageUtil.getAmazonS3().putObject(
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
			.uploadFileUrl(uploadFileUrl)
			.build();
	}

	// public void updateFile(MultipartFile multipartFile, String filePath, String bucketName) {
	// 	ObjectMetadata objectMetadata = new ObjectMetadata();
	// 	objectMetadata.setContentLength(multipartFile.getSize());
	// 	objectMetadata.setContentType(multipartFile.getContentType());
	//
	// 	try (InputStream inputStream = multipartFile.getInputStream()) {
	// 		String keyName = getFileName(filePath, bucketName);
	//
	// 		// S3에 폴더 및 파일 업데이트
	// 		/**
	// 		 * Amazon S3에서는 동일한 이름(키)을 가진 파일을 업로드하면 기존 파일을 덮어씁니다.
	// 		 * 따라서, 같은 이름의 새로운 파일을 업로드하면 기존 파일이 자동으로 업데이트됩니다.
	// 		 * 별도로 삭제 과정을 거칠 필요는 없습니다.
	// 		 */
	// 		amazonS3Client.putObject(
	// 			new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata).withCannedAcl(
	// 				CannedAccessControlList.PublicRead));
	//
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }

	public void deleteFile(String filePath, String bucketName) {
		if(filePath == null) return;
		ncpImageUtil.getAmazonS3().deleteObject(new DeleteObjectRequest(bucketName, getFileName(filePath, bucketName)));
	}

	public Optional<MultipartFile> checkFileByType(MultipartFile multipartFile, FileType fileType) {
		if (multipartFile != null && multipartFile.getContentType().startsWith(fileType.getType())) {
			return Optional.of(multipartFile);
		}
		return Optional.empty();
	}

	public String getAlbumFileUrl(String filepath) {
		return ncpImageUtil.getEndPoint() + "/" + filepath;
	}

	public String getFileName(String filepath, String bucketName) {
		String replacedFilePath = filepath.replace(bucketName, "");
		return replacedFilePath.substring(replacedFilePath.lastIndexOf("/") + 1);
	}
}