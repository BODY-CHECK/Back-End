package org.example.bodycheck.domain.mapping.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeekRoutineDTO {

    Long memberId;
    String exercise;
    Integer routineIdx;
    Integer weekId;
    Boolean routineCheck;
}
