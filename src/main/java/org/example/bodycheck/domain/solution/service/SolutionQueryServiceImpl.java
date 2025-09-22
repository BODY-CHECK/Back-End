package org.example.bodycheck.domain.solution.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apipayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.GeneralException;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.solution.converter.SolutionConverter;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDto;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;
import org.example.bodycheck.domain.solutioncriteria.service.SolutionCriteriaQueryService;
import org.example.bodycheck.domain.solutionvideo.service.SolutionVideoQueryService;
import org.example.bodycheck.external.redis.service.RedisService;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.solution.entity.Solution;
import org.example.bodycheck.domain.solution.repository.SolutionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionQueryServiceImpl implements SolutionQueryService {

    private final SolutionRepository solutionRepository;
    private final RedisService redisService;
    private final SolutionVideoQueryService solutionVideoQueryService;
    private final SolutionCriteriaQueryService solutionCriteriaQueryService;

    @Override
    public Optional<Solution> findSolution(Long id) {
        return solutionRepository.findById(id);
    }

    @Override
    public List<Solution> getSolutionList(Long memberId, String exerciseType, Integer period, Integer page) {
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

        LocalDateTime cursor;
        Integer pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize + 1, Sort.by("createdAt").descending());

        if (page == 0) {
            cursor = LocalDateTime.now();
        } else {
            if (redisService.existKey("cursor:" + memberId)) {
                String cursor_value = redisService.getValues("cursor:" + memberId);
                cursor = LocalDateTime.parse(cursor_value);
            }
            else {
                throw new GeneralHandler(ErrorStatus.SOLUTION_PAGE_NOT_FOUND);
            }
        }

        List<Solution> solutionList = solutionRepository.findSolutions(memberId, type, year, month, cursor, pageable);

        if (solutionList.isEmpty()) {
            if (page != 0) {
                throw new GeneralHandler(ErrorStatus.SOLUTION_PAGE_NOT_FOUND);
            }

            return solutionList;
        }

        boolean hasNext = solutionList.size() > pageSize;
        if (hasNext) {
            LocalDateTime new_cursor = solutionList.get(pageSize - 1).getCreatedAt();
            redisService.saveKeyValueWithTTL("cursor:" + memberId, String.valueOf(new_cursor), 1000 * 60 * 60);
        }
        else {
            LocalDateTime new_cursor = solutionList.getLast().getCreatedAt();
            redisService.saveKeyValueWithTTL("cursor:" + memberId, String.valueOf(new_cursor), 1000 * 60 * 60);
        }

        return solutionList;
    }

    @Override
    public SolutionResponseDto.SolutionDetailDto getSolutionDetail(Long solutionId) {
        String url = solutionVideoQueryService.getUrl(solutionId);

        List<SolutionCriteria> solutionCriteriaList = solutionCriteriaQueryService.getSolutionCriteriaList(solutionId);

        String content = getSolutionContent(solutionId);

        return SolutionConverter.toSolutionDetailDTO(url, solutionCriteriaList, content);
    }

    private String getSolutionContent(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(() -> new GeneralException(ErrorStatus.SOLUTION_NOT_FOUND));

        return solution.getContent();
    }
}
