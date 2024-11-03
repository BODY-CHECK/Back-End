package org.example.bodycheck.domain.mapping.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.example.bodycheck.domain.mapping.entity.Routine;

import java.util.List;

public class RoutineRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineDTO {
        @Min(1)
        @Max(7)
        Integer weekId;
        @Min(1)
        @Max(value = 3, message = "운동은 최대 3개까지 등록 가능합니다.")
        Integer routineIdx;
        String exercise;

    }
}
