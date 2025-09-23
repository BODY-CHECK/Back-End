package org.example.bodycheck.external.aws.s3.controller;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.external.aws.s3.service.AmazonS3Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Deprecated
@RestController
@RequiredArgsConstructor
public class AmazonS3TestController { // 테스트 용

	private final AmazonS3Service amazonS3Service;

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ApiResponse<String> uploadFile(@RequestPart MultipartFile file) {

		String url = amazonS3Service.uploadFile(file);

		return ApiResponse.onSuccess(url);
	}
}
