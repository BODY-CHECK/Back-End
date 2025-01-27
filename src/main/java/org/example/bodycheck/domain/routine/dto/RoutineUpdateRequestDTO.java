package org.example.bodycheck.domain.routine.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineUpdateRequestDTO {
    List<RoutineUpdateDTO> routines;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoutineUpdateDTO {
        Integer weekId;
        Integer routineIdx; // 루틴 인덱스
        Long exerciseId; // 운동 ID
        Boolean isUpdated; // 수정 여부
    }
}
