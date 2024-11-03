package org.example.bodycheck.domain.routine.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.routine.dto.*;
import org.example.bodycheck.domain.routine.entity.Routine;
import org.example.bodycheck.domain.routine.repository.RoutineRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    public void setRoutine(Member member) {
        if (member.getRoutineList() == null) {
            member.setRoutineList(new ArrayList<>()); // `routineList`가 `null`이면 초기화
        }

        List<Routine> routines = new ArrayList<>();
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
    }

//    public List<RoutineRequestDTO.RoutineDTO> setRoutine(Member member) {
//        List<Routine> routines = new ArrayList<>();
//        for (Integer weekId = 1; weekId <= 7; weekId++) {
//            for (Integer routineInx = 1; routineInx <= 3; routineInx++) {
//                Routine routine = Routine.builder()
//                        .weekId(weekId)
//                        .routineIdx(routineInx)
//                        .exercise(null)
//                        .build();
//
//                routine.setMember(member);
//                routines.add(routine);
//            }
//        }
//        routineRepository.saveAll(routines);
//
//        return routines.stream()
//                .map(routine -> RoutineRequestDTO.RoutineDTO.builder()
//                        .weekId(routine.getWeekId())
//                        .routineIdx(routine.getRoutineIdx())
//                        .exercise(null)
//                        .build()
//                )
//                .collect(Collectors.toList());
//    }

    public List<WeekRoutineDTO> getWeekRoutine(Integer weekId, Member member) {

        List<Routine> routines = routineRepository.findByMemberIdAndWeekId(member.getId(), weekId);

        if (routines.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.ROUTINE_WEEK_NOT_FOUND);
        }

        return routines.stream()
                .map(routine -> WeekRoutineDTO.builder()
                        .weekId(routine.getWeekId())
                        .routineIdx(routine.getRoutineIdx())
                        .exercise(routine.getExercise() != null ? routine.getExercise().getName() : null)
                        .build())
                .collect(Collectors.toList());

    }

    public List<RoutineUpdateRequestDTO.RoutineUpdateDTO> updateRoutine(Member member, RoutineUpdateRequestDTO routineUpdateRequestDTO) {
        List<Routine> existingRoutines = routineRepository.findByMemberId(member.getId());
        List<RoutineUpdateRequestDTO.RoutineUpdateDTO> updatedRoutines = new ArrayList<>();

        for (RoutineUpdateRequestDTO.RoutineUpdateDTO updateDTO : routineUpdateRequestDTO.getRoutines()) {
            Optional<Routine> existingRoutineOpt = existingRoutines.stream()
                    .filter(routine -> routine.getWeekId().equals(updateDTO.getWeekId()) &&
                            routine.getRoutineIdx().equals(updateDTO.getRoutineIdx()))
                    .findFirst();

            if (existingRoutineOpt.isPresent()) {
                Routine existingRoutine = existingRoutineOpt.get();

                if (updateDTO.getIsUpdated() != null && updateDTO.getIsUpdated()) {
                    if (updateDTO.getExerciseId() != null) {
                        Exercise updatedExercise = exerciseRepository.findById(Long.valueOf(updateDTO.getExerciseId()))
                                .orElseThrow(() -> new GeneralHandler(ErrorStatus.EXERCISE_NOT_FOUND));
                        existingRoutine.setExercise(updatedExercise); // Exercise 객체 업데이트
                    } else {
                        existingRoutine.setExercise(null);
                    }
                    routineRepository.save(existingRoutine);
                    updatedRoutines.add(updateDTO);
                }
            }
        }
        return updatedRoutines;
    }

    public RoutineCheckDTO checkRoutine(Member member, RoutineCheckDTO routineCheckDTO) {
        Routine routine = routineRepository.findById(routineCheckDTO.getRoutineId())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.ROUTINE_NOT_FOUND));
        if(!Objects.equals(routine.getMember().getId(), member.getId())){
            throw new GeneralHandler(ErrorStatus.TOKEN_MISSING_AUTHORITY);
        }
        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
        int currentWeekId = currentDayOfWeek.getValue();

        if (routine.getWeekId() != currentWeekId) {
            throw new GeneralHandler(ErrorStatus.INVALID_ROUTINE_CHECK); // 요일이 맞지 않으면 예외 처리
        }

        routine.setRoutineCheck(true);
        routineRepository.save(routine);

        return RoutineCheckDTO.builder()
                .routineId(routine.getId())
                .build();

    }

    public RoutineResetCheckDTO resetCheck(Member member) {

        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
        int currentWeekId = currentDayOfWeek.getValue();

        List<Routine> routines = routineRepository.findByMemberId(member.getId());
        boolean wasAnyRoutineReset = false;


        for (Routine routine : routines) {
            if (routine.getWeekId() != currentWeekId) {
                routine.setRoutineCheck(false);
            }
        }
        routineRepository.saveAll(routines);

        return RoutineResetCheckDTO.builder()
                .routineCheck(wasAnyRoutineReset)
                .build();
    }
}
