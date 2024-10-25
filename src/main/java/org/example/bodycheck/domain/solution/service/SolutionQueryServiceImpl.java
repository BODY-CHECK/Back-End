package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.GeneralException;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionQueryServiceImpl implements SolutionQueryService {

    private final SolutionRepository solutionRepository;

    @Override
    public Optional<Solution> findSolution(Long id) {
        return solutionRepository.findById(id);
    }

    @Override
    public Slice<Solution> getSolutionList(Long memberId, String exerciseType, Integer period, Integer page) {
        if (!exerciseType.equals("NULL") && !exerciseType.equals("UPPER_BODY") && !exerciseType.equals("LOWER_BODY")) {
            throw new GeneralHandler(ErrorStatus.EXERCISE_TYPE_NOT_FOUND);
        }

        ExerciseType type = null;

        if (!exerciseType.equals("NULL")) {
            type = ExerciseType.valueOf(exerciseType);
        }

        LocalDate currentDate = LocalDate.now();
        Integer year = 0;
        Integer month = 0;
        switch (period) {
            case 0:
                break;
            case 1:
                year = currentDate.getYear();
                month = currentDate.getMonthValue();
                break;
            case 2:
                year = currentDate.minusMonths(1).getYear();
                month = currentDate.minusMonths(1).getMonthValue();
                break;
            case 3:
                year = currentDate.minusMonths(2).getYear();
                month = currentDate.minusMonths(2).getMonthValue();
                break;
            case 4:
                year = currentDate.minusMonths(3).getYear();
                month = currentDate.minusMonths(3).getMonthValue();
                break;
            default:
                throw new GeneralHandler(ErrorStatus.SOLUTION_CATEGORY_NOT_FOUND);
        }

        Slice<Solution> solutionList = solutionRepository.findSolutions(memberId, type, year, month, PageRequest.of(page, 10));

        if (solutionList.isEmpty() && page != 0) {
            throw new GeneralHandler(ErrorStatus.SOLUTION_PAGE_NOT_FOUND);
        }

        return solutionList;
    }

    @Override
    public String getSolutionContent(Long solutionId, Long memberId) {
        Solution solution = solutionRepository.findByIdAndMember_Id(solutionId, memberId).orElseThrow(() -> new GeneralException(ErrorStatus.SOLUTION_NOT_FOUND));

        return solution.getContent();
    }
}
