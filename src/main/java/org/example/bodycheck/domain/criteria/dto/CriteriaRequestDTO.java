package org.example.bodycheck.domain.criteria.dto;

import lombok.Getter;

public class CriteriaRequestDTO {

    @Getter
    public static class CriteriaDTO {
        Integer criteriaIdx;
        String criteriaName;
        Integer score;
    }
}
