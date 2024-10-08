package org.example.bodycheck.domain.criteria.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;
import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDTO;
import org.example.bodycheck.domain.criteria.entity.Criteria;

public class CriteriaConverter {

    public static Criteria toCriteria(CriteriaRequestDTO.CriteriaDTO request) {
        return Criteria.builder()
                .criteriaIdx(request.getCriteriaIdx())
                .criteriaName(request.getCriteriaName())
                .score(request.getScore())
                .build();
    }

    public static CriteriaResponseDTO.CriteriaDetailDTO toCriteriaDetailDTO(Criteria criteria) {
        return CriteriaResponseDTO.CriteriaDetailDTO.builder()
                .criteriaIdx(criteria.getCriteriaIdx())
                .criteriaName(criteria.getCriteriaName())
                .score(criteria.getScore())
                .build();
    }
}
