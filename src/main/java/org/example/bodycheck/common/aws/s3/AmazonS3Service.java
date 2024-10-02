package org.example.bodycheck.common.aws.s3;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.uuid.entity.Uuid;
import org.example.bodycheck.domain.uuid.repository.UuidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmazonS3Service {
    // 테스트 용

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
