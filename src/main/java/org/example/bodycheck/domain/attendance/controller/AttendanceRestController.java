package org.example.bodycheck.domain.attendance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.validation.annotation.ExistExercise;
import org.example.bodycheck.domain.attendance.dto.AttendanceCheckDTO;
import org.example.bodycheck.domain.attendance.dto.AttendanceDTO;
import org.example.bodycheck.domain.attendance.entity.Attendance;
import org.example.bodycheck.domain.attendance.service.AttendanceService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.solution.dto.SolutionRequestDTO;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "출석 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendances")
public class AttendanceRestController {

    private final AttendanceService attendanceService;

    @PostMapping("/check/exercise/{exerciseId}")
    @Operation(summary = "출석 체크", description = "운동에 대한 출석 체크 API 입니다.")
    @Parameters({
            @Parameter(name = "exerciseId", description = "어떤 운동인지 판단하는 운동 아이디, path variable 입니다!")
    })
    public ApiResponse<AttendanceCheckDTO> checkAttendance(@AuthUser Member member,
                                     @ExistExercise @PathVariable("exerciseId") Long exerciseId,
                                     @RequestBody SolutionRequestDTO.PromptDTO request) {
        return ApiResponse.onSuccess(attendanceService.check(member, exerciseId, request));
    }

//    @Operation(summary = "출석 체크")
//    @PostMapping("/check")
//    public ApiResponse<AttendanceCheckDTO> checkAttendance(@AuthUser Member member) {
//        return ApiResponse.onSuccess(attendanceService.check(member));
//    }

    @Operation(summary = "출석 조회")
    @GetMapping("/list")
    public ApiResponse<List<AttendanceDTO>> checkAttendance(@AuthUser Member member, @RequestParam String yearMonth) {
        return ApiResponse.onSuccess(attendanceService.getAttendance(member, yearMonth));
    }
}
