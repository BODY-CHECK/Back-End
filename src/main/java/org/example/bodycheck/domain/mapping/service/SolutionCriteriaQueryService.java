package org.example.bodycheck.domain.mapping.service;

import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;

import java.util.List;

public interface SolutionCriteriaQueryService {
    List<SolutionCriteria> getSolutionCriteriaList(Long solutionId);
}
