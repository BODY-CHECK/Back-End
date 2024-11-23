package org.example.bodycheck.domain.mapping.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;
import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDTO;
import org.example.bodycheck.domain.mapping.entity.SolutionCriteria;

public class SolutionCriteriaConverter {

    public static SolutionCriteria toSolutionCriteria(CriteriaRequestDTO.CriteriaDTO request) {
        return SolutionCriteria.builder()
                .score(request.getScore())
                .build();
    }

    public static CriteriaResponseDTO.CriteriaDetailDTO toCriteriaDetailDTO(SolutionCriteria solutionCriteria) {
        return CriteriaResponseDTO.CriteriaDetailDTO.builder()
                .criteriaIdx(solutionCriteria.getCriteria().getCriteriaIdx())
                .criteriaName(solutionCriteria.getCriteria().getCriteriaName())
                .score(solutionCriteria.getScore())
                .build();
    }
}
