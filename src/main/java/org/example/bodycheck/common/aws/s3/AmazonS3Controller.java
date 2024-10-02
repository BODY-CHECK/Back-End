package org.example.bodycheck.common.aws.s3;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AmazonS3Controller {
    // 테스트 용

    private final AmazonS3Service amazonS3Service;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<String> uploadFile(@RequestPart MultipartFile file) {

        String url = amazonS3Service.uploadFile(file);

        return ApiResponse.onSuccess(url);
    }
}
