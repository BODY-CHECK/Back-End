package org.example.bodycheck.domain.criteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CriteriaResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaDetailDTO {
        Integer criteriaIdx;
        String criteriaName;
        Integer score;
    }
}
