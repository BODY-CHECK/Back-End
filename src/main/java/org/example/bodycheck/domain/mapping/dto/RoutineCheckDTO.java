package org.example.bodycheck.domain.mapping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RoutineCheckDTO {
    Long memberId;
    Long routineId;
}
