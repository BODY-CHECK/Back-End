package org.example.bodycheck.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

public class AttendanceResponseDto {

    @Getter
    @Builder
    public static class AttendanceCheckDto {
        Boolean checked;
        int grade;
        String message;
    }

    @Getter
    @Builder
    public static class AttendanceDto {
        int grade;
        String date;
    }
}
