package org.example.bodycheck.domain.mapping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeekRequestDTO {
    Long memberId;
    Integer weekId;

    public void setWeekId(Integer weekId) {
        this.weekId = weekId;
    }
}
