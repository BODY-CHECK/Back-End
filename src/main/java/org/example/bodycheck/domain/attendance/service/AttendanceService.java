package org.example.bodycheck.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.attendance.dto.AttendanceCheckDTO;
import org.example.bodycheck.domain.attendance.dto.AttendanceDTO;
import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.attendance.repository.AttendanceRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.routine.entity.Routine;
import org.example.bodycheck.domain.routine.repository.RoutineRepository;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final RoutineRepository routineRepository;

    @Transactional
    public AttendanceCheckDTO check(Member member, Long exerciseId, SolutionRequestDTO.PromptDTO request) {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        int dayNumber = (dayOfWeek.getValue() % 7) + 1;

        List<Routine> routines = routineRepository.findByMemberIdAndWeekId(member.getId(), dayNumber);

        for (Routine routine : routines) {
            if (routine.getExercise() != null && routine.getExercise().getId() != null &&
                    routine.getExercise().getId().equals(exerciseId)) { // exerciseId 비교
                routine.setRoutineCheck(true); // check 값을 true로 설정
                routineRepository.save(routine);
            }
        }

        double avgScore = request.getCriteria().stream()
                .mapToInt(criteriaItem -> criteriaItem.getScore())
                .average()
                .orElse(0.0);

        int grade;
        if (avgScore <= 20) {
            grade = 1;
        } else if (avgScore <= 40) {
            grade = 2;
        } else if (avgScore <= 60) {
            grade = 3;
        } else if (avgScore <= 80) {
            grade = 4;
        } else {
            grade = 5;
        }


        // 이미 출석한 경우를 확인 (member와 해당 날짜의 출석 여부)
        boolean hasCheckedToday = attendanceRepository.existsByMemberAndDate(member, today);

        Attendance attendance;
        if (!hasCheckedToday) {
            attendance = Attendance.builder()
                    .member(member)
                    .date(today) // LocalDate 그대로 저장
                    .grade(grade)
                    .build();
            attendanceRepository.save(attendance);

            return AttendanceCheckDTO.builder()
                    .checked(true)
                    .grade(grade)
                    .message("출석이 완료되었습니다.")
                    .build();
        }

        attendance = attendanceRepository.findByMemberAndDate(member, today).orElseThrow(() -> new GeneralHandler(ErrorStatus.ATTENDANCE_NOT_FOUND));

        if (attendance.getGrade() < grade) {
            attendance.setGrade(grade);
            attendanceRepository.save(attendance);
        }

        return AttendanceCheckDTO.builder()
                .checked(true)
                .grade(grade)
                .message("출석이 완료되었습니다.")
                .build();
    }

    @Transactional
    public List<AttendanceDTO> getAttendance(Member member, String yearMonth) {

        YearMonth targetYearMonth;

        try {
            targetYearMonth = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy.MM"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid yearMonth format. Expected format: yyyy.MM");
        }

        LocalDate startOfMonth = targetYearMonth.atDay(1); // 해당 월의 첫 날
        LocalDate endOfMonth = targetYearMonth.atEndOfMonth(); // 해당 월의 마지막 날

        List<Attendance> attendanceList = attendanceRepository.findAllByMemberAndDateBetween(member, startOfMonth, endOfMonth);

        return attendanceList.stream()
                .map(attendance -> AttendanceDTO.builder()
                        .grade(attendance.getGrade())
                        .date(attendance.getDate().toString()) // "yyyy.MM.dd" 형식으로 반환
                        .build())
                .collect(Collectors.toList());
    }
}
