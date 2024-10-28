package org.example.bodycheck.domain.mapping.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.mapping.dto.*;
import org.example.bodycheck.domain.mapping.entity.Routine;
import org.example.bodycheck.domain.mapping.repository.RoutineRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;

    public List<RoutineRequestDTO.RoutineDTO> setRoutine(RoutineSettingDTO routineSettingDTO) {
        List<Routine> routines = new ArrayList<>();

        Member member = memberRepository.findById(routineSettingDTO.getMemberId()).orElseThrow(() -> new UsernameNotFoundException(""));
        for (Integer weekId = 1; weekId <= 7; weekId++) {
            for (Integer routineInx = 1; routineInx <= 3; routineInx++) {
                Routine routine = Routine.builder()
                        .weekId(weekId)
                        .routineIdx(routineInx)
                        .exercise(null)
                        .build();

                routine.setMember(member);
                routines.add(routine);
            }
        }
        routineRepository.saveAll(routines);

        return routines.stream()
                .map(routine -> RoutineRequestDTO.RoutineDTO.builder()
                        .weekId(routine.getWeekId())
                        .routineIdx(routine.getRoutineIdx())
                        .exercise(null)
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<WeekRoutineDTO> getWeekRoutine(WeekRequestDTO weekRequestDTO) {

        List<Routine> routines = routineRepository.findByMemberIdAndWeekId(weekRequestDTO.getMemberId(), weekRequestDTO.getWeekId());

        Member member = memberRepository.findById(weekRequestDTO.getMemberId()).orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        if (routines.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.ROUTINE_WEEK_NOT_FOUND);
        }

        return routines.stream()
                .map(routine -> WeekRoutineDTO.builder()
                        .memberId(member.getId())
                        .weekId(routine.getWeekId())
                        .routineIdx(routine.getRoutineIdx())
                        .exercise(routine.getExercise() != null ? routine.getExercise().getName() : null)
                        .build())
                .collect(Collectors.toList());

    }

    public List<RoutineUpdateRequestDTO.RoutineUpdateDTO> updateRoutine(RoutineUpdateRequestDTO routineUpdateRequestDTO) {
        List<Routine> existingRoutines = routineRepository.findByMemberId(routineUpdateRequestDTO.getMemberId());
        List<RoutineUpdateRequestDTO.RoutineUpdateDTO> updatedRoutines = new ArrayList<>();

        for (RoutineUpdateRequestDTO.RoutineUpdateDTO updateDTO : routineUpdateRequestDTO.getRoutines()) {
            // 기존 루틴에서 weekId와 routineIdx가 일치하는 루틴을 찾습니다.
            Optional<Routine> existingRoutineOpt = existingRoutines.stream()
                    .filter(routine -> routine.getWeekId().equals(updateDTO.getWeekId()) &&
                            routine.getRoutineIdx().equals(updateDTO.getRoutineIdx()))
                    .findFirst();

            // 변경된 사항이 있는 경우 업데이트합니다.
            if (existingRoutineOpt.isPresent()) {
                Routine existingRoutine = existingRoutineOpt.get();

                if (updateDTO.getIsUpdated() != null && updateDTO.getIsUpdated()) {
                    if (updateDTO.getExerciseId() != null) {
                        // exerciseId가 null이 아닐 경우 새로운 Exercise로 업데이트합니다.
                        Exercise updatedExercise = exerciseRepository.findById(Long.valueOf(updateDTO.getExerciseId()))
                                .orElseThrow(() -> new GeneralHandler(ErrorStatus.EXERCISE_NOT_FOUND));
                        existingRoutine.setExercise(updatedExercise); // Exercise 객체 업데이트
                    } else {
                        // exerciseId가 null일 경우 기존 Exercise를 null로 설정합니다.
                        existingRoutine.setExercise(null);
                    }
                    routineRepository.save(existingRoutine);
                    updatedRoutines.add(updateDTO);
                }
            }
        }
        // 수정된 루틴 목록을 반환합니다.
        return updatedRoutines;

    }
}
