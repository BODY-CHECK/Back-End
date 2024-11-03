package org.example.bodycheck.domain.voiceList.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.apiPayload.code.status.SuccessStatus;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.voiceList.dto.VoiceListDTO;
import org.example.bodycheck.domain.voiceList.service.VoiceListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "VOICE API")
@RestController
@RequiredArgsConstructor
public class VoiceListController {

    private final VoiceListService voiceListService;

    @GetMapping("/api/voice/{voiceCode}/example")
    @Operation(summary = "특정 목소리 오디오 경로 반환하는 API")
    public ApiResponse<VoiceListDTO> fetchVoiceExample(@PathVariable("voiceCode") String voiceCode){
        return ApiResponse.of(SuccessStatus.OK, voiceListService.fetchVoiceList(voiceCode));
    }
}
