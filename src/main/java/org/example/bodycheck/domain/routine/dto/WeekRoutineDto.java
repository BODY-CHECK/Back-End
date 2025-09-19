package org.example.bodycheck.domain.routine.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeekRoutineDto {

    String exercise;
    Integer routineIdx;
    Integer weekId;
    Boolean routineCheck;
}
