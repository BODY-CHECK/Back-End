package org.example.bodycheck.domain.mapping.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.mapping.dto.RoutineDTO;
import org.example.bodycheck.domain.mapping.entity.Routine;
import org.example.bodycheck.domain.mapping.repository.RoutineRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final MemberCommandService memberCommandService;

    public RoutineDTO createRoutine(RoutineDTO routineDTO) {


        Member member = memberCommandService.findById(routineDTO.getMemberId());

        Exercise exercise = exerciseRepository.findByName(routineDTO.getExercise())
                .orElseThrow(() -> new IllegalArgumentException("Invalid exercise ID: " + routineDTO.getExercise()));

        boolean exists = routineRepository.existsByMemberAndWeekIdAndRoutineIdx(member, routineDTO.getWeekId(), routineDTO.getRoutineIdx());
        if (exists) {
            throw new IllegalArgumentException("A routine with the same weekId and routineIdx already exists for this member.");
        }

        Routine routine = Routine.builder()
                .member(member)
                .weekId(routineDTO.getWeekId())
                .routineIdx(routineDTO.getRoutineIdx())
                .exercise(exercise)  // 운동 이름을 직접 사용하므로 Exercise 객체 생성
                .build();

        // Routine 엔티티 저장
        routineRepository.save(routine);

        // 저장된 Routine 엔티티를 DTO로 변환하여 반환
        return RoutineDTO.builder()
                .memberId(member.getId())
                .weekId(routine.getWeekId())
                .routineIdx(routine.getRoutineIdx())
                .exercise(exercise.getName())
                .build();
    }

    @Transactional
    public RoutineDTO deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new NoSuchElementException("루틴이 존재하지 않습니다"));

        routineRepository.delete(routine);
        return new RoutineDTO(routine);
    }
}
