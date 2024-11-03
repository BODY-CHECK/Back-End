package org.example.bodycheck.domain.tts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.apiPayload.code.status.SuccessStatus;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.tts.dto.*;
import org.example.bodycheck.domain.tts.service.TTSService;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "TTS API", description = "tts 관련 api입니다")
@RequestMapping("/api/tts")
public class TTSController {

    private final TTSService ttsService;

    @PostMapping("/count/{exerciseId}/{ttsIdx}")
    @Operation(summary = "횟수 출력")
    public ApiResponse<GetDTO> getCount(@PathVariable("exerciseId") Long exerciseId, @PathVariable("ttsIdx") Integer ttsIdx, @AuthUser Member member) {
        return ApiResponse.of(SuccessStatus.OK, ttsService.getContent(exerciseId, ttsIdx, member));
    }

    @PostMapping("/{exerciseId}/{ttsIdx}")
    @Operation(summary = "솔루션 출력")
    public ApiResponse<GetDTO> getContext(@PathVariable("exerciseId") Long exerciseId, @PathVariable("ttsIdx") Integer ttsIdx, @AuthUser Member member) {
        return ApiResponse.of(SuccessStatus.OK, ttsService.getContent(exerciseId, ttsIdx, member));
    }
}
