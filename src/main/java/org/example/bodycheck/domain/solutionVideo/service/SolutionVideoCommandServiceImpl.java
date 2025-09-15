package org.example.bodycheck.domain.solutionVideo.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solutionVideo.converter.SolutionVideoConverter;
import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;
import org.example.bodycheck.domain.solutionVideo.repository.SolutionVideoRepository;
import org.example.bodycheck.external.aws.s3.service.AmazonS3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionVideoCommandServiceImpl implements SolutionVideoCommandService {

    private final SolutionVideoRepository solutionVideoRepository;

    private final AmazonS3Service amazonS3Service;

    @Override
    @Transactional
    public void uploadFile(Solution solution, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String url = amazonS3Service.uploadFile(file);

            SolutionVideo solutionVideo = SolutionVideoConverter.toSolutionVideo(solution, url);

            solutionVideoRepository.save(solutionVideo);
        }
    }
}
