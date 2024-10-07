package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.criteria.converter.CriteriaConverter;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCommandServiceImpl implements SolutionCommandService {

    private final SolutionRepository solutionRepository;
    private final CriteriaRepository criteriaRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public Solution saveSolution(Long memberId, Long exerciseId, SolutionRequestDTO.SaveDTO request) {
        Solution solution = SolutionConverter.toSolution(request);
        solution.setMember(memberRepository.findById(memberId).get());
        solution.setExercise(exerciseRepository.findById(exerciseId).get());
        Solution solutionEntity = solutionRepository.save(solution);
        request.getCriteria().stream()
                .forEach(criteriaItem -> {
                    Criteria criteria = CriteriaConverter.toCriteria(criteriaItem);
                    criteria.setSolution(solutionEntity);
                    criteriaRepository.save(criteria);
                });

        return solutionEntity;
    }
}
