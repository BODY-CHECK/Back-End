package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface SolutionQueryService {

    Optional<Solution> findSolution(Long id);

    Slice<Solution> getSolutionList(Long memberId, Integer page);

    String getSolutionContent(Long solutionId, Long memberId);
}
