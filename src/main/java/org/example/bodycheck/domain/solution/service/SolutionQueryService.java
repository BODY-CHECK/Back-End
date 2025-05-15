package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.solution.entity.Solution;

import java.util.List;
import java.util.Optional;

public interface SolutionQueryService {

    Optional<Solution> findSolution(Long id);

    List<Solution> getSolutionList(Long memberId, String exerciseType, Integer period, Integer page);

    String getSolutionContent(Long solutionId, Long memberId);
}
