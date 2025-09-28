package org.example.bodycheck.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

public class AttendanceResponseDto {

	@Getter
	@Builder
	public static class AttendanceCheckDto {
		private Boolean checked;
		private int grade;
		private String message;
	}

	@Getter
	@Builder
	public static class AttendanceDto {
		private int grade;
		private String date;
	}
}
