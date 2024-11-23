package org.example.bodycheck.domain.mapping.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;
import org.example.bodycheck.domain.mapping.repository.SolutionCriteriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
