package org.example.bodycheck.external.aws.s3.manager;

import java.io.IOException;

import org.example.bodycheck.common.config.AmazonConfig;
import org.example.bodycheck.domain.uuid.entity.Uuid;
import org.example.bodycheck.domain.uuid.repository.UuidRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

	private final AmazonS3 amazonS3;

	private final AmazonConfig amazonConfig;

	private final UuidRepository uuidRepository;

	public String uploadFile(String keyName, MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());

		String originalFilename = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
		}

		String finalKeyName = keyName + fileExtension;

		try {
			amazonS3.putObject(
				new PutObjectRequest(amazonConfig.getBucket(), finalKeyName, file.getInputStream(), metadata));
		} catch (IOException e) {
			log.error("error at AmazonS3Manager uploadFile : {}", (Object)e.getStackTrace());
		}

		return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
	}

	public String generateReviewKeyName(Uuid uuid) {
		return amazonConfig.getSolutionPath() + '/' + uuid.getUuid();
	}
}
