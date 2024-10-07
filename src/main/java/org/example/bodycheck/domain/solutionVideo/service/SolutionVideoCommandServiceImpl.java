package org.example.bodycheck.domain.solutionVideo.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.aws.s3.AmazonS3Manager;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solutionVideo.converter.SolutionVideoConverter;
import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;
import org.example.bodycheck.domain.solutionVideo.repository.SolutionVideoRepository;
import org.example.bodycheck.domain.uuid.entity.Uuid;
import org.example.bodycheck.domain.uuid.repository.UuidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionVideoCommandServiceImpl implements SolutionVideoCommandService {

    private final AmazonS3Manager amazonS3Manager;

    private final SolutionVideoRepository solutionVideoRepository;
    private final UuidRepository uuidRepository;

    @Override
    @Transactional
    public void uploadFile(Solution solution, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            Uuid saveUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

            String url = amazonS3Manager.uploadFile(amazonS3Manager.generateReviewKeyName(saveUuid), file);

            SolutionVideo solutionVideo = SolutionVideoConverter.toSolutionVideo(solution, url);

            solutionVideoRepository.save(solutionVideo);
        }
    }
}
