package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.criteria.converter.CriteriaConverter;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCommandServiceImpl implements SolutionCommandService {

    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public String generateSolution(Long memberId, Long exerciseId, SolutionRequestDTO.PromptDTO request) {
        String criteriaText = request.getCriteria().stream()
                .map(criteriaItem -> criteriaItem.getCriteriaIdx() + ". " + criteriaItem.getCriteriaName() + " / " + criteriaItem.getScore() + "\n")
                .collect(Collectors.joining());

        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new GeneralHandler(ErrorStatus.EXERCISE_NOT_FOUND));

        String prompt = "운동 이름: " + exercise.getName() + "\n평가 지표 / 점수:\n" + criteriaText;
        return prompt;
    }

    @Override
    @Transactional
    public Solution saveSolution(Long memberId, Long exerciseId, SolutionRequestDTO.SaveDTO request) {
        Solution solution = SolutionConverter.toSolution(request);
        solution.setMember(memberRepository.findById(memberId).get());
        solution.setExercise(exerciseRepository.findById(exerciseId).get());

        return solutionRepository.save(solution);
    }
}
