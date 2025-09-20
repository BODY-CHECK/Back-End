package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDto;
import org.example.bodycheck.domain.criteria.entity.Criteria;
import org.example.bodycheck.domain.criteria.repository.CriteriaRepository;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDto;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.example.bodycheck.domain.solutioncriteria.service.SolutionCriteriaCommandService;
import org.example.bodycheck.domain.solutionvideo.service.SolutionVideoCommandService;
import org.example.bodycheck.external.openai.service.OpenAIService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionCommandServiceImpl implements SolutionCommandService {

    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;
    private final CriteriaRepository criteriaRepository;
    private final OpenAIService openAIService;
    private final SolutionCriteriaCommandService solutionCriteriaCommandService;
    private final SolutionVideoCommandService solutionVideoCommandService;

    @Override
    @Transactional
    public String generateSolution(Long memberId, Long exerciseId, SolutionRequestDto.PromptDto request) {
        List<Criteria> criterias = criteriaRepository.findByExercise_Id(exerciseId);

        String criteriaText = IntStream.range(0, criterias.size())
                .mapToObj(i -> {
                    Criteria criteria = criterias.get(i);
                    CriteriaRequestDto.CriteriaDto criteriaDTO = request.getCriteria().get(i);
                    return criteria.getCriteriaIdx() + ". " + criteria.getCriteriaName() + " / " + criteriaDTO.getScore() + "\n";
                })
                .collect(Collectors.joining());

        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new GeneralHandler(ErrorStatus.EXERCISE_NOT_FOUND));

        String prompt1 = "Your task is to respond in a consistent style. Answer in the given output format.\n"
                + "input: \n"
                + "Exercise Name: 푸쉬업\n"
                + "Criteria / Score:\n"
                + "1. 팔의 구부림 각도가 정확한가 / 100\n"
                + "2. 자세가 일직선으로 정렬되어 있는가 / 50\n"
                + "3. 무릎의 구부림 각도가 정확한가 / 75\n"
                + "output:\n"
                + "총점: 75 / 100\n"
                + "팔을 잘 구부립니다! 하지만 푸쉬업을 할 때 허리를 곧게 피고, 무릎을 잘 구부리는 것이 중요하니, 신경써서 다시 시도해보세요!\n"
                + "input: \n";
        String prompt2 = "Exercise Name: " + exercise.getName() + "\nCriteria / Score:\n" + criteriaText;
        String prompt3 = "\noutput: ";
        String prompt =  prompt1 + prompt2 + prompt3;

        return openAIService.chat(prompt);
    }

    @Override
    @Transactional
    public Solution saveSolution(Member member, Long exerciseId, SolutionRequestDto.SaveDto request, MultipartFile file) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new GeneralHandler(ErrorStatus.EXERCISE_NOT_FOUND));
        Solution solution = SolutionConverter.toSolution(request, member, exercise);

        solutionRepository.save(solution);

        solutionCriteriaCommandService.saveSolutionCriteria(solution, exerciseId, request);
        solutionVideoCommandService.uploadFile(solution, file);

        return solution;
    }
}
