package org.example.bodycheck.domain.routine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

public class RoutineRequestDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineDto {
        @Min(1)
        @Max(7)
        Integer weekId;
        @Min(1)
        @Max(value = 3, message = "운동은 최대 3개까지 등록 가능합니다.")
        Integer routineIdx;
        String exercise;

    }

    @Getter
    public static class RoutineIdDto {
        private Long routineId;
    }

    @Getter
    public static class RoutineUpdateRequestDto {
        private List<RoutineUpdateDto> routines;
    }

    @Getter
    public static class RoutineUpdateDto {
        private Integer weekId;
        private Integer routineIdx; // 루틴 인덱스
        private Long exerciseId; // 운동 ID
        private Boolean isUpdated; // 수정 여부
    }
}
