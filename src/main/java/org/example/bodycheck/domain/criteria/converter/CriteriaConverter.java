package org.example.bodycheck.domain.criteria.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;
import org.example.bodycheck.domain.criteria.entity.Criteria;

public class CriteriaConverter {

    public static Criteria toCriteria(CriteriaRequestDTO.SaveDTO request) {
        return Criteria.builder()
                .criteriaIdx(request.getCriteriaIdx())
                .criteriaName(request.getCriteriaName())
                .score(request.getScore())
                .build();
    }
}
