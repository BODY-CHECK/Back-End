package org.example.bodycheck.domain.solution.service;

import java.util.List;
import java.util.Optional;

import org.example.bodycheck.domain.solution.dto.SolutionResponseDto;
import org.example.bodycheck.domain.solution.entity.Solution;

public interface SolutionQueryService {

	Optional<Solution> findSolution(Long id);

	List<Solution> getSolutionList(Long memberId, String exerciseType, Integer period, Integer page);

	SolutionResponseDto.SolutionDetailDto getSolutionDetail(Long solutionId);
}
