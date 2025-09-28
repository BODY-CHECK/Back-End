package org.example.bodycheck.domain.solutioncriteria.service;

import java.util.List;

import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;

public interface SolutionCriteriaQueryService {

	List<SolutionCriteria> getSolutionCriteriaList(Long solutionId);
}
