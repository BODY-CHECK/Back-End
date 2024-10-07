package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface SolutionCommandService {
    Solution saveSolution(Long memberId, Long exerciseId, SolutionRequestDTO.SaveDTO request);
}
