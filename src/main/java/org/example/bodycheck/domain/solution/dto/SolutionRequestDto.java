package org.example.bodycheck.domain.solution.dto;

import lombok.Getter;
import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDto;

import java.util.List;

public class SolutionRequestDto {

    @Getter
    public static class PromptDto {
        List<CriteriaRequestDto.CriteriaDto> criteria;
    }

    @Getter
    public static class SaveDto {
        List<CriteriaRequestDto.CriteriaDto> criteria;
        String content;
    }
}
