package org.example.bodycheck.domain.solutioncriteria.service;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface SolutionCriteriaCommandService {

	void saveSolutionCriteria(Solution solution, Long exerciseId, SolutionRequestDto.SaveDto request);
}
