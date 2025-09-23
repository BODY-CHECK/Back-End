package org.example.bodycheck.external.aws.s3.service;

import java.util.UUID;

import org.example.bodycheck.domain.uuid.entity.Uuid;
import org.example.bodycheck.domain.uuid.repository.UuidRepository;
import org.example.bodycheck.external.aws.s3.manager.AmazonS3Manager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

	private final AmazonS3Manager amazonS3Manager;
	private final UuidRepository uuidRepository;

	@Transactional
	public String uploadFile(MultipartFile file) {
		String uuid = UUID.randomUUID().toString();
		Uuid saveUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

		String url = amazonS3Manager.uploadFile(amazonS3Manager.generateReviewKeyName(saveUuid), file);

		return url;
	}
}
