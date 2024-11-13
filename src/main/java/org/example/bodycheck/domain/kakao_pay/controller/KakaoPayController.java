package org.example.bodycheck.domain.kakao_pay.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.kakao_pay.converter.KakaoPayConverter;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.kakao_pay.service.KakaoPayService;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ApiResponse<KakaoPayDTO.KakaoReadyResponse> readyToKakaoPay(@AuthUser Member member) {
        KakaoPayDTO.KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady();

        Long memberId = member.getId();

        kakaoPayService.saveTid(memberId, kakaoReadyResponse.getTid());

        return ApiResponse.onSuccess(kakaoReadyResponse);
    }

    @GetMapping("/success")
    public ModelAndView afterPayRequest(@RequestParam("pg_token") String pgToken) {
        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);

        ModelAndView modelAndView = new ModelAndView("success"); // "success"는 템플릿 파일 이름
        modelAndView.addObject("paymentInfo", kakaoApproveResponse);

        kakaoPayService.saveSid(kakaoApproveResponse);

        return modelAndView;
    }

    @GetMapping("/fail")
    public String fail() {
        return "fail";
    }

    @GetMapping("/cancel")
    public ApiResponse<KakaoPayDTO.KakaoCancelResponse> refund(@AuthUser Member member) {

        Long memberId = member.getId();

        KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

        KakaoPayDTO.KakaoCancelResponse kakaoCancelResponse = kakaoPayService.cancelResponse(kakaoPay.getTid());

        kakaoPayService.cancelPay(memberId);

        return ApiResponse.onSuccess(kakaoCancelResponse);
    }

    @PostMapping("/subscribe")
    public ApiResponse<KakaoPayDTO.KakaoApproveResponse> subscribePayRequest(@AuthUser Member member) {
        Long memberId = member.getId();

        KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveSubscribeResponse(kakaoPay.getSid());

        kakaoPayService.savePayInfo(memberId, kakaoApproveResponse);

        return ApiResponse.onSuccess(kakaoApproveResponse);
    }

    @PostMapping("/subscribe/cancel")
    public ApiResponse<KakaoPayDTO.KakaoSubscribeCancelResponse> subscribeCancelRequest(@AuthUser Member member) {
        Long memberId = member.getId();

        KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

        KakaoPayDTO.KakaoSubscribeCancelResponse kakaoSubscribeCancelResponse = kakaoPayService.subscribeCancelResponse(kakaoPay.getSid());

        return ApiResponse.onSuccess(kakaoSubscribeCancelResponse);
    }

    @GetMapping("/subscribe/status")
    public ApiResponse<KakaoPayDTO.KakaoPayStatus> subscribeStatusRequest(@AuthUser Member member) {
        Long memberId = member.getId();

        boolean isLogExist = kakaoPayService.getKakaoPayLog(memberId);

        KakaoPayDTO.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = new KakaoPayDTO.KakaoSubscribeStatusResponse();

        if (isLogExist) {
            KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

            kakaoSubscribeStatusResponse = kakaoPayService.subscribeStatusResponse(kakaoPay.getSid());
        }

        return ApiResponse.onSuccess(KakaoPayConverter.toKakaoPayStatus(isLogExist, kakaoSubscribeStatusResponse));
    }
}
