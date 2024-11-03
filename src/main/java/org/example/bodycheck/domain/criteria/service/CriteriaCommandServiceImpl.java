package org.example.bodycheck.domain.criteria.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.criteria.converter.CriteriaConverter;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CriteriaCommandServiceImpl implements CriteriaCommandService {
    private final CriteriaRepository criteriaRepository;

    @Override
    @Transactional
    public void saveCriteria(Solution solution, SolutionRequestDTO.SaveDTO request) {
        if (solution.getCriteriaList() == null) {
            solution.setCriteriaList(new ArrayList<>()); // `criteriaList`가 `null`이면 초기화
        }
        request.getCriteria().stream()
                .forEach(criteriaItem -> {
                    Criteria criteria = CriteriaConverter.toCriteria(criteriaItem);
                    criteria.setSolution(solution);
                    criteriaRepository.save(criteria);
                });
    }
}
