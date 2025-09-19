package org.example.bodycheck.external.kakao.pay.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.external.kakao.pay.dto.KakaoPayDto;
import org.example.bodycheck.external.kakao.pay.service.KakaoPayService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    @Operation(summary = "카카오페이 URL 생성 API", description = "카카오페이 URL을 생성하는 API 입니다.")
    public ApiResponse<KakaoPayDto.KakaoReadyResponse> readyToKakaoPay(@AuthUser Member member) {
        KakaoPayDto.KakaoReadyResponse kakaoReadyResponse = kakaoPayService.readyToKakaoPay(member.getId());

        return ApiResponse.onSuccess(kakaoReadyResponse);
    }

    @GetMapping("/approve/callback")
    public ApiResponse<String> getPgToken(@RequestParam("pg_token") String pgToken) {
        return ApiResponse.onSuccess(pgToken);
    }

    @PostMapping("/approve")
    @Operation(summary = "카카오페이 결제 승인", description = "카카오페이 결제를 승인하는 API 입니다.")
    public ApiResponse<String> approvePayment(@AuthUser Member member, @RequestBody KakaoPayDto.KakaoApproveRequest request) {
        kakaoPayService.approvePayment(member.getId(), request.getPgToken(), request.getTid());

        return ApiResponse.onSuccess("OK");
    }

//    @GetMapping("/approve")
//    public ModelAndView afterPayRequest(@RequestParam("pg_token") String pgToken) {
//        KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);
//
//        ModelAndView modelAndView = new ModelAndView("success"); // "success"는 템플릿 파일 이름
//        modelAndView.addObject("paymentInfo", kakaoApproveResponse);
//
//        kakaoPayService.saveSid(kakaoApproveResponse);
//
//        return modelAndView;
//    }

    @GetMapping("/fail/callback")
    public String fail() {
        return "fail";
    }

    @GetMapping("/cancel/callback")
    public String cancel() {
        return "cancel";
    }

    @DeleteMapping("/cancel")
    public ApiResponse<String> refund(@AuthUser Member member) {
        kakaoPayService.refund(member.getId());

        return ApiResponse.onSuccess("OK");
    }

    @PostMapping("/subscribe")
    @Operation(summary = "카카오페이 구독 API", description = "카카오페이를 구독하는 API 입니다.")
    public ApiResponse<KakaoPayDto.KakaoApproveResponse> subscribePayRequest(@AuthUser Member member) {
        KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.subscribeKakaoPay(member.getId());

        return ApiResponse.onSuccess(kakaoApproveResponse);
    }

    @PostMapping("/subscribe/cancel")
    @Operation(summary = "카카오페이 구독 취소 API", description = "카카오페이 구독을 취소하는 API 입니다.")
    public ApiResponse<KakaoPayDto.KakaoSubscribeCancelResponse> subscribeCancelRequest(@AuthUser Member member) {
        KakaoPayDto.KakaoSubscribeCancelResponse kakaoSubscribeCancelResponse = kakaoPayService.subscribeKakaoPayCancel(member.getId());

        return ApiResponse.onSuccess(kakaoSubscribeCancelResponse);
    }

    @GetMapping("/subscribe/status")
    @Operation(summary = "카카오페이 구독 상태 확인 API", description = "카카오페이 구독 상태를 확인하는 API 입니다.")
    public ApiResponse<KakaoPayDto.KakaoPayStatus> subscribeStatusRequest(@AuthUser Member member) {
        KakaoPayDto.KakaoPayStatus kakaoPayStatus = kakaoPayService.subcribeKakaoPayStatus(member.getId());

        return ApiResponse.onSuccess(kakaoPayStatus);
    }
}
