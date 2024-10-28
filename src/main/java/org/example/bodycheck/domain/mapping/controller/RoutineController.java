package org.example.bodycheck.domain.mapping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.apiPayload.code.status.SuccessStatus;
import org.example.bodycheck.domain.mapping.dto.RoutineDTO;
import org.example.bodycheck.domain.mapping.service.RoutineService;
import org.example.bodycheck.domain.mapping.service.RoutineServiceImpl;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberQueryService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "루틴 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routine")
public class RoutineController {
    private final RoutineService routineService;
    private final MemberQueryService memberQueryService;


    @Operation(summary = "루틴 저장 API")
    @PostMapping("/save")
    public ApiResponse<RoutineDTO> saveRoutine(@Valid @RequestBody RoutineDTO routineDTO) {

        Long memberId = memberQueryService.getMember().getId();
        return ApiResponse.onSuccess(routineService.createRoutine(memberId, routineDTO));
    }

    @Operation(summary = "루틴 삭제 API")
    @DeleteMapping("/{routineId}")
    public ApiResponse<RoutineDTO> deleteRoutine(@PathVariable("routineId") Long routineId) {
        return ApiResponse.of(SuccessStatus.OK, routineService.deleteRoutine(routineId));
    }
}
