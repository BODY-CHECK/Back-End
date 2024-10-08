package org.example.bodycheck.domain.solution.service;

import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.data.domain.Slice;

public interface SolutionQueryService {

    Slice<Solution> getSolutionList(Long memberId, Integer page);
}
