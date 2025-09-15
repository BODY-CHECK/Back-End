package org.example.bodycheck.external.kakao_pay.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.external.kakao_pay.converter.KakaoPayConverter;
import org.example.bodycheck.external.kakao_pay.dto.KakaoPayDto;
import org.example.bodycheck.external.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.external.kakao_pay.repository.KakaoPayRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@EnableScheduling
public class KakaoPayService {

    private RestTemplate restTemplate = new RestTemplate();
    private KakaoPayDto.KakaoReadyResponse kakaoReadyResponse;
    private final KakaoPayRepository kakaoPayRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.kakaopay.secret_key}")
    private String secretKey;

    @Value("${spring.kakaopay.cid}")
    private String cid;

    @Value("${spring.kakaopay.domain}")
    private String domain;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "SECRET_KEY " + secretKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    public KakaoPayDto.KakaoReadyResponse kakaoPayReady() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("cid", cid);
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("item_name", "BodyCheck 구독");
        parameters.put("quantity", "1");
        parameters.put("total_amount", "4900");
        parameters.put("vat_amount", "200");
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", domain + "/payment/success"); // http://localhost:8080/payment/success
        parameters.put("fail_url", domain + "/payment/fail"); // http://localhost:8080/payment/fail
        parameters.put("cancel_url", domain + "/payment/cancel"); // http://localhost:8080/payment/cancel


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        kakaoReadyResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoPayDto.KakaoReadyResponse.class);
        return kakaoReadyResponse;
    }

    public KakaoPayDto.KakaoApproveResponse approveResponse(String pgToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", kakaoReadyResponse.getTid());
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoPayDto.KakaoApproveResponse.class);
        return kakaoApproveResponse;
    }

    public KakaoPayDto.KakaoCancelResponse cancelResponse(String tid) {
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

        KakaoPayDto.KakaoCancelResponse kakaoCancelResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                requestEntity,
                KakaoPayDto.KakaoCancelResponse.class);
        return kakaoCancelResponse;
    }

    public KakaoPay getKakaoPayInfo(Long memberId) {
        KakaoPay kakaoPay =  kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        return kakaoPay;
    }

    public boolean getKakaoPayLog(Long memberId) {
        return kakaoPayRepository.existsByMember_Id(memberId);
    }

    public boolean getPremiumState(Long memberId) {
        boolean isPremium = false;

        if (kakaoPayRepository.existsByMember_Id(memberId)) {
            KakaoPay kakaoPay =  kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

            if (kakaoPay.getSid() == null || kakaoPay.getSid().isEmpty()) {}
            else {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("cid", cid);
                parameters.put("sid", kakaoPay.getSid());

                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

                KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = restTemplate.postForObject(
                        "https://open-api.kakaopay.com/online/v1/payment/manage/subscription/status",
                        requestEntity,
                        KakaoPayDto.KakaoSubscribeStatusResponse.class);

                if (kakaoSubscribeStatusResponse.getStatus().equals("ACTIVE")) {
                    isPremium = true;
                }
                else {
                    String last_approved_at;
                    if(kakaoSubscribeStatusResponse.getLast_approved_at() == null || kakaoSubscribeStatusResponse.getLast_approved_at().isEmpty()) {
                        last_approved_at = kakaoSubscribeStatusResponse.getCreated_at();
                    }
                    else last_approved_at = kakaoSubscribeStatusResponse.getLast_approved_at();

                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    LocalDateTime lastApprovedAt = LocalDateTime.parse(last_approved_at, formatter);

                    LocalDateTime oneMonthLater = lastApprovedAt.plusMonths(1).withHour(14).withMinute(0).withSecond(0);

                    LocalDateTime now = LocalDateTime.now();

                    if (now.isBefore(oneMonthLater)) {
                        isPremium = true;
                    }
                }
            }

        }

        return isPremium;
    }

    public KakaoPayDto.KakaoApproveResponse approveSubscribeResponse(String sid) {
        if (sid == null || sid.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.SID_NOT_EXIST);
        }

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

        KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/subscription",
                requestEntity,
                KakaoPayDto.KakaoApproveResponse.class);
        return kakaoApproveResponse;
    }

    public KakaoPayDto.KakaoSubscribeCancelResponse subscribeCancelResponse(String sid) {
        if (sid == null || sid.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.SID_NOT_EXIST);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("sid", sid);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDto.KakaoSubscribeCancelResponse kakaoSubscribeCancelResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/manage/subscription/inactive",
                requestEntity,
                KakaoPayDto.KakaoSubscribeCancelResponse.class);
        return kakaoSubscribeCancelResponse;
    }

    public KakaoPayDto.KakaoSubscribeStatusResponse subscribeStatusResponse(String sid) {
        if (sid == null || sid.isEmpty()) {
            throw new GeneralHandler(ErrorStatus.SID_NOT_EXIST);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("sid", sid);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/manage/subscription/status",
                requestEntity,
                KakaoPayDto.KakaoSubscribeStatusResponse.class);
        return kakaoSubscribeStatusResponse;
    }

    public void saveTid(Long memberId, String tid) {
        KakaoPay kakaoPay;
        if (kakaoPayRepository.existsByMember_Id(memberId)) {
            kakaoPay = kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
            kakaoPay.updateTid(tid);
        }
        else {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
            kakaoPay = KakaoPayConverter.toKakaoPayTid(tid, member);
        }
        kakaoPayRepository.save(kakaoPay);
    }

    public void saveSid(KakaoPayDto.KakaoApproveResponse kakaoApproveResponse) {

        KakaoPay kakaoPay = kakaoPayRepository.findByTid(kakaoApproveResponse.getTid()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        kakaoPay.updateSid(kakaoApproveResponse.getSid());

        kakaoPayRepository.save(kakaoPay);
    }

    public void savePayInfo(Long memberId, KakaoPayDto.KakaoApproveResponse kakaoApproveResponse) {
        KakaoPay kakaoPay;
        if (kakaoPayRepository.existsByMember_Id(memberId)) {
            kakaoPay = kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
            kakaoPay.updatePayInfo(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid());
        }
        else {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
            kakaoPay = KakaoPayConverter.toKakaoPay(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid(), member);
        }
        kakaoPayRepository.save(kakaoPay);
    }

    public void cancelPay(Long memberId) {
        KakaoPay kakaoPay = kakaoPayRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));

        kakaoPayRepository.delete(kakaoPay);
    }

    @Scheduled(cron = "0 0 14 * * ?")
    public void regularPayment() {
        List<KakaoPay> kakaoPayList = kakaoPayRepository.findAllWithMemberAndSidNotNull();

        kakaoPayList.stream()
                .forEach(kakaoPay -> {
                    if (kakaoPay.getSid() != null && !kakaoPay.getSid().isEmpty()) {
                        KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = subscribeStatusResponse(kakaoPay.getSid());

                        // "ACTIVE" 상태인지 확인
                        if (kakaoSubscribeStatusResponse.getStatus().equals("ACTIVE")) {
                            String lastApprovedAtStr = kakaoSubscribeStatusResponse.getLast_approved_at();
                            if (lastApprovedAtStr == null || lastApprovedAtStr.isEmpty()) {
                                lastApprovedAtStr = kakaoSubscribeStatusResponse.getCreated_at();
                            }

                            // last_approved_at을 LocalDate로 변환
                            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                            LocalDate lastApprovedAt = LocalDate.parse(lastApprovedAtStr, formatter);

                            LocalDate today = LocalDate.now();

                            // 결제일과 오늘의 일(day)이 같고, 마지막 결제일이 이번 달이 아닌 경우에만 결제 수행
                            if (today.getDayOfMonth() == lastApprovedAt.getDayOfMonth() &&
                                    (today.getYear() != lastApprovedAt.getYear() || today.getMonthValue() != lastApprovedAt.getMonthValue())) {

                                KakaoPayDto.KakaoApproveResponse approveResponse = approveSubscribeResponse(kakaoPay.getSid());

                                savePayInfo(kakaoPay.getMember().getId(), approveResponse);
                            }
//                            if (today.getDayOfMonth() == lastApprovedAt.getDayOfMonth()) {
//                                KakaoPayDto.KakaoApproveResponse approveResponse = approveSubscribeResponse(kakaoPay.getSid());
//
//                                savePayInfo(kakaoPay.getMember().getId(), approveResponse);
//                            }
                        }
                    }
                });

//        System.out.println("정기 결제 작업 완료");
    }
}
