package org.example.bodycheck.domain.mapping.service;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface SolutionCriteriaCommandService {
    void saveSolutionCriteria(Solution solution, Long exerciseId, SolutionRequestDTO.SaveDTO request);
}
