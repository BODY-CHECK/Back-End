package org.example.bodycheck.domain.solutionVideo.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.GeneralException;
import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;
import org.example.bodycheck.domain.solutionVideo.repository.SolutionVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionVideoQueryServiceImpl implements SolutionVideoQueryService {

    private final SolutionVideoRepository solutionVideoRepository;

    @Override
    public String getUrl(Long solutionId) {

        String url = "";
        if (solutionVideoRepository.existsBySolution_Id(solutionId)) {
            SolutionVideo solutionVideo = solutionVideoRepository.findBySolution_Id(solutionId).orElseThrow(() -> new GeneralException(ErrorStatus.SOLUTION_VIDEO_NOT_FOUND));
            url = solutionVideo.getVideoUrl();
        }

        return url;
    }
}
