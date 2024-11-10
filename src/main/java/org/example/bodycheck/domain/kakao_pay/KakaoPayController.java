package org.example.bodycheck.domain.kakao_pay;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/success/ok")
    public ApiResponse<KakaoPayDTO.KakaoApproveResponse> afterPayRequest(@AuthUser Member member, @RequestParam("pg_token") String pgToken) {
        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);

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

        KakaoPayDTO.KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(member.getTid());

        return ApiResponse.onSuccess(kakaoCancelResponse);
    }

    @PostMapping("/ready")
    public KakaoPayDTO.KakaoReadyResponse readyToKakaoPay(@AuthUser Member member) {
        return kakaoPayService.kakaoPayReady();
    }
}
