package org.example.bodycheck.domain.solution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class SolutionResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionResultDTO {
        Long id;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionInfoDTO {
        Long exerciseId;
        String exerciseName;
        String exerciseDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionListDTO {
        List<SolutionInfoDTO> solutionList;
        Integer listSize;
        Boolean isFirst;
        Boolean isLast;
        Boolean hasNext;
    }
}
