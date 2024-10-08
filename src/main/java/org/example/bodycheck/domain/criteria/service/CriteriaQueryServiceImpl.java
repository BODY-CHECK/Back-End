package org.example.bodycheck.domain.criteria.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CriteriaQueryServiceImpl implements CriteriaQueryService {

    private final CriteriaRepository criteriaRepository;

    @Override
    public List<Criteria> getCriteriaList(Long solutionId) {

        return criteriaRepository.findBySolution_Id(solutionId);
    }
}
