package org.example.bodycheck.domain.mapping.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.mapping.converter.SolutionCriteriaConverter;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;
import org.example.bodycheck.domain.mapping.repository.SolutionCriteriaRepository;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCriteriaCommandServiceImpl implements SolutionCriteriaCommandService {
    private final SolutionCriteriaRepository solutionCriteriaRepository;
    private final CriteriaRepository criteriaRepository;

    @Override
    @Transactional
    public void saveSolutionCriteria(Solution solution, Long exerciseId, SolutionRequestDTO.SaveDTO request) {
        if (solution.getSolutionCriteriaList() == null) {
            solution.setSolutionCriteriaList(new ArrayList<>()); // `criteriaList`가 `null`이면 초기화
        }
        List<Criteria> criterias = criteriaRepository.findByExercise_Id(exerciseId);

        IntStream.range(0, criterias.size())
                .forEach(i -> {
                    Criteria criteria = criterias.get(i); // 인덱스를 기준으로 Criteria 가져오기
                    CriteriaRequestDTO.CriteriaDTO criteriaDTO = request.getCriteria().get(i); // 동일 인덱스의 CriteriaDTO 가져오기

                    // SolutionCriteria 변환 및 설정
                    SolutionCriteria solutionCriteria = SolutionCriteriaConverter.toSolutionCriteria(criteriaDTO);
                    solutionCriteria.setSolution(solution); // Solution 설정
                    solutionCriteria.setCriteria(criteria); // 매핑된 Criteria 설정

                    // Repository에 저장
                    solutionCriteriaRepository.save(solutionCriteria);
                });
    }
}
