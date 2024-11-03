package org.example.bodycheck.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.attendance.dto.AttendanceCheckDTO;
import org.example.bodycheck.domain.attendance.dto.AttendanceDTO;
import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.attendance.repository.AttendanceRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceCheckDTO check(Member member) {

        LocalDate today = LocalDate.now();

        // 이미 출석한 경우를 확인 (member와 해당 날짜의 출석 여부)
        boolean hasCheckedToday = attendanceRepository.existsByMemberAndDate(member, today);

        if (hasCheckedToday) {
            return AttendanceCheckDTO.builder()
                    .checked(true)
                    .message("이미 오늘 출석을 완료하였습니다.")
                    .build();
        }

        // 출석 기록이 없으면 새로 저장
        Attendance attendance = Attendance.builder()
                .member(member)
                .date(today) // LocalDate 그대로 저장
                .build();
        attendanceRepository.save(attendance);

        return AttendanceCheckDTO.builder()
                .checked(true)
                .message("출석이 완료되었습니다.")
                .build();
    }

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
                        .date(attendance.getDate().toString()) // "yyyy.MM.dd" 형식으로 반환
                        .memberId(member.getId())
                        .build())
                .collect(Collectors.toList());
    }
}
