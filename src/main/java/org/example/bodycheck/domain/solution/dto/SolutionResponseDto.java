package org.example.bodycheck.domain.solution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDto;

import java.util.List;

public class SolutionResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionResultDto {
        Long id;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionInfoDto {
        Long id;
        Long exerciseId;
        String exerciseName;
        String exerciseDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionListDto {
        List<SolutionInfoDto> solutionList;
        Integer listSize;
        Boolean isFirst;
        Boolean isLast;
        Boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionDetailDto {
        String solutionVideoUrl;
        List<CriteriaResponseDto.CriteriaDetailDto> criteriaDetailList;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertSolutionDto {
        String solutionVideoUrl;
    }
}
