package org.example.bodycheck.domain.kakao_pay.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.kakao_pay.converter.KakaoPayConverter;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.kakao_pay.repository.KakaoPayRepository;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@EnableScheduling
public class KakaoPayService {

    private RestTemplate restTemplate = new RestTemplate();
    private KakaoPayDTO.KakaoReadyResponse kakaoReadyResponse;
    private final KakaoPayRepository kakaoPayRepository;
    private final MemberRepository memberRepository;

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
        parameters.put("item_name", "BodyCheck 구독");
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

    public KakaoPayDTO.KakaoCancelResponse cancelResponse(String tid) {
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

    public KakaoPayDTO.KakaoApproveResponse approveSubscribeResponse(String sid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("sid", sid);
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("item_name", "BodyCheck 구독");
        parameters.put("quantity", "1");
        parameters.put("total_amount", "4900");
        parameters.put("vat_amount", "200");
        parameters.put("tax_free_amount", "0");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/subscription",
                requestEntity,
                KakaoPayDTO.KakaoApproveResponse.class);
        return kakaoApproveResponse;
    }

    public KakaoPayDTO.KakaoSubscribeCancelResponse subscribeCancelResponse(String sid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("sid", sid);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDTO.KakaoSubscribeCancelResponse kakaoSubscribeCancelResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/manage/subscription/inactive",
                requestEntity,
                KakaoPayDTO.KakaoSubscribeCancelResponse.class);
        return kakaoSubscribeCancelResponse;
    }

    public KakaoPayDTO.KakaoSubscribeStatusResponse subscribeStatusResponse(String sid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("sid", sid);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDTO.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/manage/subscription/status",
                requestEntity,
                KakaoPayDTO.KakaoSubscribeStatusResponse.class);
        return kakaoSubscribeStatusResponse;
    }

    public void saveTid(Long memberId, String tid) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        KakaoPay kakaoPay;
        if (kakaoPayRepository.existsByMember_Id(member.getId())) {
            kakaoPay = kakaoPayRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
            kakaoPay.setTid(tid);
        }
        else {
            kakaoPay = KakaoPayConverter.toKakaoPayTid(tid, member);
        }
        kakaoPayRepository.save(kakaoPay);
    }

    public void saveSid(KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse) {

        KakaoPay kakaoPay = kakaoPayRepository.findByTid(kakaoApproveResponse.getTid()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        kakaoPay.setSid(kakaoApproveResponse.getSid());

        kakaoPayRepository.save(kakaoPay);
    }

    public void savePayInfo(Long memberId, KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        KakaoPay kakaoPay;
        if (kakaoPayRepository.existsByMember_Id(member.getId())) {
            kakaoPay = kakaoPayRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
            kakaoPay.setTid(kakaoApproveResponse.getTid());
            kakaoPay.setSid(kakaoApproveResponse.getSid());
        }
        else {
            kakaoPay = KakaoPayConverter.toKakaoPay(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid(), member);
        }
        kakaoPayRepository.save(kakaoPay);
    }

    public void cancelPay(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        KakaoPay kakaoPay = kakaoPayRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        kakaoPayRepository.delete(kakaoPay);
    }

    @Scheduled(cron = "* * 14 * * ?")
    public void regularPayment() {
        System.out.println("정기 결제");
    }
}
