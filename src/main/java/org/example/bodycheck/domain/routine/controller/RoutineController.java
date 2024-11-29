package org.example.bodycheck.domain.routine.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.openai.OpenAIService;
import org.example.bodycheck.domain.routine.dto.*;
import org.example.bodycheck.domain.routine.service.RoutineService;

import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "루틴 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineService routineService;
    private final OpenAIService openAIService;

//    @Operation(summary = "루틴 생성 API")
//    @PostMapping("/setting")
//    public ApiResponse<List<RoutineRequestDTO.RoutineDTO>> setting(@AuthUser Member member) {
//        return ApiResponse.onSuccess(routineService.setRoutine(member));
//    }

    @Operation(summary = "루틴 조회 API")
    @GetMapping("/list/{weekId}")
    public ApiResponse<List<WeekRoutineDTO>> getRoutineList(@PathVariable("weekId") Integer weekId, @AuthUser Member member) {
        return ApiResponse.onSuccess(routineService.getWeekRoutine(weekId, member));
    }

    @Operation(summary = "루틴 수정 API")
    @PostMapping("/update")
    public ApiResponse<List<RoutineUpdateRequestDTO.RoutineUpdateDTO>> updateRoutine(@AuthUser Member member,
                                                                                     @Valid @RequestBody RoutineUpdateRequestDTO request) {
        return ApiResponse.onSuccess(routineService.updateRoutine(member, request));
    }

    @Operation(summary = "루틴 운동 체크 API")
    @PostMapping("/check")
    public ApiResponse<RoutineCheckDTO> updateRoutineCheck(@AuthUser Member member, @RequestBody RoutineCheckDTO request){
        return ApiResponse.onSuccess(routineService.checkRoutine(member, request));
    }

    @Operation(summary = "루틴 운동 체크 해제 API")
    @PostMapping("/resetCheck")
    public ApiResponse<RoutineResetCheckDTO> resetCheck(@AuthUser Member member){
        return ApiResponse.onSuccess(routineService.resetCheck(member));
    }

    @Operation(summary = "루틴 랜덤 4개 출력 API")
    @PostMapping("/random")
    public ApiResponse<List<RoutineRandomDTO>> randomRoutine(@AuthUser Member member){
        return ApiResponse.onSuccess(routineService.randomRoutine(member));
    }

    @PostMapping(value = "/recommendation", consumes = "multipart/form-data")
    @Operation(summary = "AI 추천 루틴 제공 API")
    public ApiResponse<String> recommendationRoutine(@AuthUser Member member,
                                                     @RequestPart(value = "image", required = false) MultipartFile image,
                                                     @RequestPart(value = "prompt") @Valid String request) throws IOException {

        String prompt = routineService.generateRoutine(request);

        String response;
        if (image == null) {
            response = openAIService.chat(prompt);
        }
        else {
            response = openAIService.vision(image, prompt);
        }

        return ApiResponse.onSuccess(response);
    }
}
