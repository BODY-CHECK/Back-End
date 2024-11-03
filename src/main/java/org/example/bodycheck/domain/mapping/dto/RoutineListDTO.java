package org.example.bodycheck.domain.mapping.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.bodycheck.domain.criteria.dto.CriteriaRequestDTO;

import java.util.List;

public class RoutineListDTO {

    @Getter
    @Builder
    public static class RoutineDTO {
        List<RoutineRequestDTO.RoutineDTO> routine;
    }
}
