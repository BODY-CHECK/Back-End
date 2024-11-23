package org.example.bodycheck.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceDTO {
    int grade;
    String date;
}
