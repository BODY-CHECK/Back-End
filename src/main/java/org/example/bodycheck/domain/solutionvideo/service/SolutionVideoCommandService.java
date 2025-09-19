package org.example.bodycheck.domain.solutionvideo.service;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.web.multipart.MultipartFile;

public interface SolutionVideoCommandService {

    void uploadFile(Solution solution, MultipartFile file);
}
