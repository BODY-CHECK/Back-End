package org.example.bodycheck.domain.tts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.apiPayload.code.status.SuccessStatus;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.tts.dto.TTSDTO;
import org.example.bodycheck.domain.tts.dto.TTSRequestDTO;
import org.example.bodycheck.domain.tts.dto.TTSResponseDTO;
import org.example.bodycheck.domain.tts.dto.TTSResultDTO;
import org.example.bodycheck.domain.tts.service.TTSService;
import org.springframework.http.MediaType;
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

    /* TTS 재생 API*/
    @PostMapping("/play")
    @Operation(summary = "TTS 재생 API")
    public ApiResponse<TTSResponseDTO> playTTS(@Valid @RequestBody TTSRequestDTO requestDTO,
                                               @AuthUser Member member) {
        System.out.println("POST api/cast/play for exercise ID : " + requestDTO.getExerciseId());

        TTSResultDTO resultDTO = ttsService.createSpeech(requestDTO.getExerciseId(), requestDTO.getVoice(), requestDTO.getScript());

        TTSResponseDTO responseDTO = new TTSResponseDTO(requestDTO.getExerciseId(), resultDTO.getScript(), true);
        return ApiResponse.of(SuccessStatus.OK, responseDTO);
    }


}
