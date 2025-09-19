package org.example.bodycheck.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceDto {
    int grade;
    String date;
}
