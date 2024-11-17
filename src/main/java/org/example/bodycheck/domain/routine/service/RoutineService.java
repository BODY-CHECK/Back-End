package org.example.bodycheck.domain.routine.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.exercise.entity.Exercise;
import org.example.bodycheck.domain.exercise.repository.ExerciseRepository;
import org.example.bodycheck.domain.routine.dto.*;
import org.example.bodycheck.domain.routine.entity.Routine;
import org.example.bodycheck.domain.routine.repository.RoutineRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
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

    public List<RoutineRandomDTO> randomRoutine(Member member) {
        ExerciseType exerciseType = member.getExerciseType();

        // exerciseType에 따라 운동 목록 필터링
        List<Exercise> exercises;
        if (exerciseType == ExerciseType.UPPER_BODY) {
            exercises = exerciseRepository.findByType(ExerciseType.UPPER_BODY);
        } else if (exerciseType == ExerciseType.LOWER_BODY) {
            exercises = exerciseRepository.findByType(ExerciseType.LOWER_BODY);
        } else {
            exercises = exerciseRepository.findAll();
        }

        // 4개의 랜덤 운동 선택
        Random random = new Random();
        List<Exercise> randomExercises = random.ints(0, exercises.size())
                .distinct()
                .limit(4)
                .mapToObj(exercises::get)
                .collect(Collectors.toList());

        // RoutineRandomDTO로 변환하여 반환
        return randomExercises.stream()
                .map(exercise -> RoutineRandomDTO.builder()
                        .exerciseId(exercise.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public String generateRoutine(RoutineRequestDTO.PromptDTO request) {
        String prompt1 = "Your task is to create a workout routine based on the input."
                + "You must reflect the degree of rest that the user wants."
                + "How many times you want to rest means all day long."
                + "A few days off means a whole day off and you have to reflect that number of times."
                + "If you have a break in the middle of your daily routine, you must have a break in the last place."
                + "You have to print out only the ones on your workout list."
                + "Routines must be printed from Monday to Sunday, no matter what."
                + "You have to keep the same format as the example."
                + "Regardless of the question, every routine should be printed out the entire week: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, and Sunday"
                + "Begin the response with a sentence that reflects the user's request."
                + "12 types of existing exercises and their categories:"
                + "푸쉬업, UPPER_BODY\n"
                + "푸쉬업(무릎), UPPER_BODY\n"
                + "풀업, UPPER_BODY\n"
                + "풀업(밴드), UPPER_BODY\n"
                + "윗몸일으키기, UPPER_BODY\n"
                + "레그레이즈, UPPER_BODY\n"
                + "레그레이즈(행잉), UPPER_BODY\n"
                + "스쿼드, LOWER_BODY\n"
                + "한 발 스쿼트, LOWER_BODY\n"
                + "런지, LOWER_BODY\n"
                + "카프레이즈, LOWER_BODY\n"
                + "힙 쓰러스트, LOWER_BODY\n"
                + "휴식, REST\n"
                + "\nExamples:\n"
                + "Input: 나는 상체를 키우고 싶어.\n"
                + "Output: 상체를 키우기 위한 루틴을 추천해드릴게요!\n\n"
                + "월 - 푸쉬업, 풀업, 스쿼드\n"
                + "화 - 윗몸일으키기, 레그레이즈, 힙 쓰러스트\n"
                + "수 - 풀업, 휴식, 런지\n"
                + "목 - 스쿼드, 푸쉬업, 레그레이즈\n"
                + "금 - 윗몸일으키기, 런지, 휴식\n"
                + "토 - 푸쉬업(무릎), 카프레이즈, 레그레이즈\n"
                + "일 - 휴식\n\n"
                + "Input: 나는 힙을 키우고 싶고 매일 운동하기 싫어 적당히 잘 조절해봐.\n"
                + "Output: 힙과 휴식을 중심으로 적절히 조절한 루틴을 추천해드릴게요!\n\n"
                + "월 - 스쿼트, 런지, 카프레이즈\n"
                + "화 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "수 - 휴식\n"
                + "목 - 힙 쓰러스트, 레그레이즈, 휴식\n"
                + "금 - 스쿼트, 런지, 카프레이즈\n"
                + "토 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "일 - 휴식, 힙 쓰러스트, 레그레이즈\n\n"
                + "Input: 나는 광배랑 허벅지를 키울거야 근데 일주일에 3번은 쉬어야해\n.\n"
                + "Output: 광배와 허벅지 위주의 루틴을 추천해드릴게요!\n\n"
                + "월 - 휴식\n"
                + "화 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "수 - 휴식\n"
                + "목 - 힙 쓰러스트, 레그레이즈, 휴식\n"
                + "금 - 스쿼트, 런지, 카프레이즈\n"
                + "토 - 휴식\n"
                + "일 - 휴식, 힙 쓰러스트, 레그레이즈\n\n"
                + "Input: 기초 체력을 키우고 싶어 근데 3일은 쉴래\n.\n"
                + "Output: 기초 체력 향상을 위한 루틴을 추천해드릴게요!\n\n"
                + "월 - 휴식\n"
                + "화 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "수 - 휴식\n"
                + "목 - 힙 쓰러스트, 레그레이즈, 휴식\n"
                + "금 - 스쿼트, 런지, 카프레이즈\n"
                + "토 - 휴식\n"
                + "일 - 휴식, 힙 쓰러스트, 레그레이즈\n\n"
                + "Input: 기초 체력을 키우고 싶어 근데 월요일은 쉴래\n.\n"
                + "Output: 기초 체력 향상을 위한 루틴을 추천해드릴게요!\n\n"
                + "월 - 휴식\n"
                + "화 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "수 - 풀업(밴드), 힙 쓰러스트, 런지\n"
                + "목 - 힙 쓰러스트, 레그레이즈, 휴식\n"
                + "금 - 스쿼트, 런지, 카프레이즈\n"
                + "토 - 윗몸일으키기, 카프레이즈, 스쿼드\n"
                + "일 - 푸쉬업, 레그레이즈(행잉), 런지\n\n"
                + "Input: 허벅지 위주의 운동인데 월, 수, 금은 쉴래\n.\n"
                + "Output: 허벅지 위주의 루틴을 추천해드릴게요!\n\n"
                + "월 - 휴식\n"
                + "화 - 힙 쓰러스트, 레그레이즈(행잉), 휴식\n"
                + "수 - 휴식\n"
                + "목 - 힙 쓰러스트, 레그레이즈, 휴식\n"
                + "금 - 휴식\n"
                + "토 - 윗몸일으키기, 카프레이즈, 스쿼드\n"
                + "일 - 푸쉬업, 레그레이즈(행잉), 런지\n\n";
        String prompt2 = request.getPrompt();
        String prompt3 = "\nOutput:\n";
        return prompt1 + prompt2 + prompt3;

    }
}
