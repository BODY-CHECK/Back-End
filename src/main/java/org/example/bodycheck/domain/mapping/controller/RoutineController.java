package org.example.bodycheck.domain.mapping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.mapping.dto.*;
import org.example.bodycheck.domain.mapping.service.RoutineService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "루틴 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineService routineService;

    @Operation(summary = "루틴 생성 API")
    @PostMapping("/setting")
    public ApiResponse<List<RoutineRequestDTO.RoutineDTO>> setting(@RequestBody RoutineSettingDTO request) {
        return ApiResponse.onSuccess(routineService.setRoutine(request));
    }

    @Operation(summary = "루틴 조회 API")
    @PostMapping("/list/{weekId}")
    public ApiResponse<List<WeekRoutineDTO>> getRoutineList(@PathVariable("weekId") Integer weekId, @RequestBody @Valid WeekRequestDTO request) {
        request.setWeekId(weekId);
        return ApiResponse.onSuccess(routineService.getWeekRoutine(request));
    }

    @Operation(summary = "루틴 수정 API")
    @PostMapping("/update")
    public ApiResponse<List<RoutineUpdateRequestDTO.RoutineUpdateDTO>> updateRoutine(@Valid @RequestBody RoutineUpdateRequestDTO request) {
        return ApiResponse.onSuccess(routineService.updateRoutine(request));
    }

    @Operation(summary = "루틴 운동 체크 API")
    @PostMapping("/check")
    public ApiResponse<RoutineCheckDTO> updateRoutineCheck(RoutineCheckDTO request){
        return ApiResponse.onSuccess(routineService.checkRoutine(request));
    }

    @Operation(summary = "루틴 운동 체크 해제 API")
    @PostMapping("/resetCheck")
    public ApiResponse<RoutineResetCheckDTO> resetCheck(RoutineResetCheckDTO request){
        return ApiResponse.onSuccess(routineService.resetCheck(request));
    }

}
