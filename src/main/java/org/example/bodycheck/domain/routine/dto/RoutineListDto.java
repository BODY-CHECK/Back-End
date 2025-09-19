package org.example.bodycheck.domain.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RoutineListDto {

    @Getter
    @Builder
    public static class RoutineDTO {
        List<RoutineRequestDto.RoutineDTO> routine;
    }
}
