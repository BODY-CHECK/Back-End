package org.example.bodycheck.domain.criteria.service;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface CriteriaCommandService {

    void saveCriteria(Solution solution, SolutionRequestDTO.SaveDTO request);
}
