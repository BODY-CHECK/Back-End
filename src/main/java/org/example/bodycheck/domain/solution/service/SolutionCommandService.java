package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface SolutionCommandService {
    String generateSolution(Long memberId, Long exerciseId, SolutionRequestDto.PromptDto request);
    Solution saveSolution(Long memberId, Long exerciseId, SolutionRequestDto.SaveDto request);
}
