package org.example.bodycheck.external.fcm.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.external.fcm.dto.FcmRequestDto;
import org.example.bodycheck.external.fcm.service.FcmService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {
    // 테스트 용

    private final FcmService fcmService;

    @PostMapping("/fcm/sendMessage")
    public ApiResponse<String> sendMessage(@AuthUser Member member,
                                           @RequestBody FcmRequestDto request) {
        String response = fcmService.sendMessage(member.getId(), request);

        return ApiResponse.onSuccess(response);
    }
}
