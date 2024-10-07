package org.example.bodycheck.domain.solutionVideo.converter;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solutionVideo.entity.SolutionVideo;

public class SolutionVideoConverter {

    public static SolutionVideo toSolutionVideo(Solution solution, String url) {
        return SolutionVideo.builder()
                .solution(solution)
                .videoUrl(url)
                .build();
    }
}
