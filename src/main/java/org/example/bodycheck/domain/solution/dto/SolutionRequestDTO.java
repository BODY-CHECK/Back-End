package org.example.bodycheck.domain.solution.dto;

import lombok.Getter;
import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;

import java.util.List;

public class SolutionRequestDTO {

    @Getter
    public static class SaveDTO {
        List<CriteriaRequestDTO.SaveDTO> criteria;
        String content;
    }
}
