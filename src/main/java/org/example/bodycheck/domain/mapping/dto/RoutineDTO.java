package org.example.bodycheck.domain.mapping.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.bodycheck.domain.mapping.entity.Routine;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoutineDTO {

    private Long memberId;
    @Min(1)
    @Max(7)
    private Integer weekId;
    @Min(1)
    @Max(value = 3, message = "운동은 최대 3개까지 등록 가능합니다.")
    private Integer routineIdx;
    private String exercise;

    public RoutineDTO(Routine routine) {
        memberId = routine.getId();
        weekId = routine.getWeekId();
        routineIdx = routine.getRoutineIdx();
        exercise = routine.getExercise().getName();
    }
}
