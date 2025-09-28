package org.example.bodycheck.domain.solutioncriteria.service;

import java.util.List;

import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solutioncriteria.repository.SolutionCriteriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCriteriaQueryServiceImpl implements SolutionCriteriaQueryService {

	private final SolutionCriteriaRepository solutionCriteriaRepository;

	@Override
	public List<SolutionCriteria> getSolutionCriteriaList(Long solutionId) {
		return solutionCriteriaRepository.findBySolution_Id(solutionId);
	}
}
