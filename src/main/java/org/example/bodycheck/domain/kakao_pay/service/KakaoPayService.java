package org.example.bodycheck.domain.kakao_pay.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.kakao_pay.repository.KakaoPayRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private RestTemplate restTemplate = new RestTemplate();
    private KakaoPayDTO.KakaoReadyResponse kakaoReadyResponse;
    private KakaoPayRepository kakaoPayRepository;

    @Value("${spring.kakaopay.secret_key}")
    private String secretKey;

    @Value("${spring.kakaopay.cid}")
    private String cid;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "SECRET_KEY " + secretKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    public KakaoPayDTO.KakaoReadyResponse kakaoPayReady() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("cid", cid);
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("item_name", "ITEM_NAME");
        parameters.put("quantity", "1");
        parameters.put("total_amount", "4900");
        parameters.put("vat_amount", "200");
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "https://dev.bodycheck.store/payment/success"); // http://localhost:8080/payment/success
        parameters.put("fail_url", "https://dev.bodycheck.store/payment/fail"); // http://localhost:8080/payment/fail
        parameters.put("cancel_url", "https://dev.bodycheck.store/payment/cancel"); // http://localhost:8080/payment/cancel


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        kakaoReadyResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoPayDTO.KakaoReadyResponse.class);
        return kakaoReadyResponse;
    }

    public KakaoPayDTO.KakaoApproveResponse approveResponse(String pgToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", kakaoReadyResponse.getTid());
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoPayDTO.KakaoApproveResponse.class);
        return kakaoApproveResponse;
    }

    public KakaoPayDTO.KakaoCancelResponse kakaoCancel(String tid) {
        if (tid == null || tid.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.TID_NOT_EXIST);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", tid);
        parameters.put("cancel_amount", "4900");
        parameters.put("cancel_tax_free_amount", "0");
        parameters.put("cancel_vat_amount", "0");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDTO.KakaoCancelResponse kakaoCancelResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                requestEntity,
                KakaoPayDTO.KakaoCancelResponse.class);
        return kakaoCancelResponse;
    }

    public KakaoPay getKakaoPayInfo(Long memberId) {
        KakaoPay kakaoPay =  kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        return kakaoPay;
    }


}
