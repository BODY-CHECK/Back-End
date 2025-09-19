package org.example.bodycheck.domain.solutioncriteria.service;

import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;

import java.util.List;

public interface SolutionCriteriaQueryService {
    List<SolutionCriteria> getSolutionCriteriaList(Long solutionId);
}
