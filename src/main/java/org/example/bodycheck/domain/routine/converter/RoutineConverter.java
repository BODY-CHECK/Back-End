package org.example.bodycheck.domain.routine.converter;

import org.example.bodycheck.domain.routine.dto.RoutineRequestDto;
import org.example.bodycheck.domain.routine.dto.RoutineResponseDto;

public class RoutineConverter {

	public static RoutineResponseDto.RoutineUpdateDto toResponseDto(RoutineRequestDto.RoutineUpdateDto requestDto) {
		return RoutineResponseDto.RoutineUpdateDto.builder()
			.weekId(requestDto.getWeekId())
			.routineIdx(requestDto.getRoutineIdx())
			.exerciseId(requestDto.getExerciseId())
			.isUpdated(requestDto.getIsUpdated())
			.build();
	}
}
