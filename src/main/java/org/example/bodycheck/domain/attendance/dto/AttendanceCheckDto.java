package org.example.bodycheck.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceCheckDto {
    Boolean checked;
    int grade;
    String message;
}
