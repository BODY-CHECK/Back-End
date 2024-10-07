package org.example.bodycheck.domain.solution.converter;

import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.example.bodycheck.domain.solution.dto.SolutionResponseDTO;
import org.example.bodycheck.domain.solution.entity.Solution;

public class SolutionConverter {

    public static Solution toSolution(SolutionRequestDTO.SaveDTO request) {
        return Solution.builder()
                .content(request.getContent())
                .build();
    }

    public static SolutionResponseDTO.SolutionResultDTO toSolutionResultDTO(Solution solution) {
        return SolutionResponseDTO.SolutionResultDTO.builder()
                .id(solution.getId())
                .build();
    }
}
