package org.example.bodycheck.domain.kakao_pay;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/success")
    public ResponseEntity<KakaoPayDTO.KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken) {
        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = kakaoPayService.approveResponse(pgToken);

        return new ResponseEntity<>(kakaoApproveResponse, HttpStatus.OK);
    }

    @GetMapping("/fail")
    public String fail() {
        return "fail";
    }

    @GetMapping("cancel")
    public ResponseEntity<KakaoPayDTO.KakaoCancelResponse> refund(@RequestParam("tid") String tid) {
        KakaoPayDTO.KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel(tid);

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }

    @PostMapping("/ready")
    public KakaoPayDTO.KakaoReadyResponse readyToKakaoPay() {
        return kakaoPayService.kakaoPayReady();
    }
}
