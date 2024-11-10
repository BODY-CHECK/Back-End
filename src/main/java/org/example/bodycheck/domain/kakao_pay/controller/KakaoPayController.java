package org.example.bodycheck.domain.kakao_pay.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.kakao_pay.service.KakaoPayService;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final MemberCommandService memberCommandService;

    @PostMapping("/ready")
    public ApiResponse<KakaoPayDTO.KakaoReadyResponse> readyToKakaoPay(@AuthUser Member member) {
        return ApiResponse.onSuccess(kakaoPayService.kakaoPayReady());
    }

    @GetMapping("/success/ok")
    public ApiResponse<KakaoPayDTO.KakaoApproveResponse> afterPayRequest(@AuthUser Member member, @RequestParam("pg_token") String pgToken, @RequestParam("tid") String tid) {
        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken, tid);

        Long memberId = member.getId();

        memberCommandService.savePayInfo(memberId, kakaoApproveResponse);

        return ApiResponse.onSuccess(kakaoApproveResponse);
    }

    @GetMapping("/fail/ok")
    public String fail(@AuthUser Member member) {
        return "fail";
    }

    @GetMapping("/cancel/ok")
    public ApiResponse<KakaoPayDTO.KakaoCancelResponse> refund(@AuthUser Member member) {

        Long memberId = member.getId();

        KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

        KakaoPayDTO.KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(kakaoPay.getTid());

        memberCommandService.cancelPay(memberId);

        return ApiResponse.onSuccess(kakaoCancelResponse);
    }

    @GetMapping("/regular/ok")
    public ApiResponse<KakaoPayDTO.KakaoApproveResponse> regularPayment(@AuthUser Member member) {
        Long memberId = member.getId();

        KakaoPay kakaoPay = kakaoPayService.getKakaoPayInfo(memberId);

        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveRegularResponse(kakaoPay.getTid(), kakaoPay.getSid());

        return ApiResponse.onSuccess(kakaoApproveResponse);
    }
}
