package org.example.bodycheck.domain.solutioncriteria.converter;

import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDto;
import org.example.bodycheck.domain.criteria.dto.CriteriaResponseDto;
import org.example.bodycheck.domain.solutioncriteria.entity.SolutionCriteria;

public class SolutionCriteriaConverter {

    public static SolutionCriteria toSolutionCriteria(CriteriaRequestDto.CriteriaDto request) {
        return SolutionCriteria.builder()
                .score(request.getScore())
                .build();
    }

    public static CriteriaResponseDto.CriteriaDetailDto toCriteriaDetailDTO(SolutionCriteria solutionCriteria) {
        return CriteriaResponseDto.CriteriaDetailDto.builder()
                .criteriaIdx(solutionCriteria.getCriteria().getCriteriaIdx())
                .criteriaName(solutionCriteria.getCriteria().getCriteriaName())
                .score(solutionCriteria.getScore())
                .build();
    }
}
