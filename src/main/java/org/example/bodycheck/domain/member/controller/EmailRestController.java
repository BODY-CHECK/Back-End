package org.example.bodycheck.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.member.service.EmailService.EmailCommandService;
import org.example.bodycheck.domain.member.dto.EmailDTO.EmailRequestDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members/emails")
public class EmailRestController {
    private final EmailCommandService emailCommandService;

    @PostMapping("/send-verification-code")
    @Operation(summary = "인증코드 API", description = "사용자가 입력한 이메일로 인증코드를 보내는 API 입니다.")
    public ApiResponse<String> sendVerificationEmail(@Valid @RequestBody EmailRequestDTO.EmailDTO request) {

        emailCommandService.sendVerificationEmail(request);
        return ApiResponse.onSuccess("인증 코드가 성공적으로 발급되었습니다.");
    }

    @PostMapping("/verify-code")
    @Operation(summary = "인증코드 검증 API", description = "사용자가 입력한 인증코드가 맞는지 검증하는 API 입니다.")
    public ApiResponse<String> verifyCode(@RequestBody EmailRequestDTO.VerificationDTO request) {
        boolean isVerified = emailCommandService.verifyCode(request);
        if (isVerified) {
            return ApiResponse.onSuccess("이메일 인증이 완료되었습니다.");
        }
        else {
            return ApiResponse.onFailure("400", "이메일 인증에 실패했습니다.", request.getCode() + ", 인증 코드가 틀렸습니다.");
        }
    }

    @PostMapping("/send-new-pw")
    @Operation(summary = "비밀번호 찾기(재생성) API", description = "사용자가 비밀번호를 잊었을 때 사용자 이메일로 새로운 비밀번호를 보내는 API 입니다.")
    public ApiResponse<String> sendNewPwEmail(@RequestBody EmailRequestDTO.EmailDTO request) {

        emailCommandService.sendNewPwEmail(request);
        return ApiResponse.onSuccess("임시 비밀번호가 성공적으로 발급되었습니다.");
    }
}
