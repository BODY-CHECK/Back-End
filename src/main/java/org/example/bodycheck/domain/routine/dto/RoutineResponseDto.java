package org.example.bodycheck.domain.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoutineResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeekRoutineDto {
        private String exercise;
        private Integer routineIdx;
        private Integer weekId;
        private Boolean routineCheck;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineIdDto {
        private Long routineId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineResetCheckDto {
        private Boolean routineCheck;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineRandomDto {
        private Long exerciseId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineUpdateDto {
        private Integer weekId;
        private Integer routineIdx; // 루틴 인덱스
        private Long exerciseId; // 운동 ID
        private Boolean isUpdated; // 수정 여부
    }
}
