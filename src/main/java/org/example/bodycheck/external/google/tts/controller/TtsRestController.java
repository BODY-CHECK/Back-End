package org.example.bodycheck.external.google.tts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.common.apipayload.code.status.SuccessStatus;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.external.google.tts.dto.*;
import org.example.bodycheck.external.google.tts.service.TtsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "TTS API", description = "tts 관련 api입니다")
@RequestMapping("/api/tts")
public class TtsRestController {

    private final TtsService ttsService;

    @PostMapping("/count/{ttsId}")
    @Operation(summary = "횟수 출력")
    public ApiResponse<GetDto> getCount(@PathVariable("ttsId") Long ttsId, @AuthUser Member member) {
        return ApiResponse.of(SuccessStatus.OK, ttsService.getCount(ttsId, member));
    }

    @PostMapping("/{exerciseId}/{ttsIdx}")
    @Operation(summary = "솔루션 출력")
    public ApiResponse<GetDto> getContext(@PathVariable("exerciseId") Long exerciseId, @PathVariable("ttsIdx") Integer ttsIdx, @AuthUser Member member) {
        return ApiResponse.of(SuccessStatus.OK, ttsService.getContent(exerciseId, ttsIdx, member));
    }
}
