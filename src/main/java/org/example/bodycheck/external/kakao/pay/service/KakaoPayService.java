package org.example.bodycheck.external.kakao.pay.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.bodycheck.common.apipayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.external.kakao.pay.converter.KakaoPayConverter;
import org.example.bodycheck.external.kakao.pay.dto.KakaoPayDto;
import org.example.bodycheck.external.kakao.pay.entity.KakaoPay;
import org.example.bodycheck.external.kakao.pay.repository.KakaoPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@EnableScheduling
public class KakaoPayService {

	public static final String BASE_URL = "https://open-api.kakaopay.com/online/v1/payment";
	public static final String READY_URL = BASE_URL + "/ready";
	public static final String APPROVE_URL = BASE_URL + "/approve";
	public static final String CANCEL_URL = BASE_URL + "/cancel";
	public static final String SUBSCRIBE_URL = BASE_URL + "/subscription";
	public static final String SUBSCRIBE_STATUS_URL = BASE_URL + "/manage/subscription/status";
	public static final String SUBSCRIBE_CANCEL_URL = BASE_URL + "/manage/subscription/inactive";

	private final RestTemplate restTemplate;
	private final KakaoPayRepository kakaoPayRepository;
	private final MemberRepository memberRepository;

	private final String secretKey;
	private final String cid;
	private final String domain;

	private KakaoPayDto.KakaoReadyResponse kakaoReadyResponse;

	@Autowired
	public KakaoPayService(
		KakaoPayRepository kakaoPayRepository,
		MemberRepository memberRepository,
		@Value("${spring.kakaopay.secret_key}") String secretKey,
		@Value("${spring.kakaopay.cid}") String cid,
		@Value("${spring.kakaopay.domain}") String domain) {
		this.restTemplate = new RestTemplate();
		this.kakaoPayRepository = kakaoPayRepository;
		this.memberRepository = memberRepository;
		this.secretKey = secretKey;
		this.cid = cid;
		this.domain = domain;
	}

	@Transactional
	public KakaoPayDto.KakaoReadyResponse readyToKakaoPay(Long memberId) {
		KakaoPayDto.KakaoReadyResponse response = kakaoPayReady(memberId);

		saveTid(memberId, response.getTid());

		return response;
	}

	@Transactional
	public void approvePayment(Long memberId, String pgToken, String tid) {
		KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = approveResponse(memberId, pgToken, tid);

		saveSid(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid());
	}

	@Transactional
	public void refund(Long memberId) {
		KakaoPay kakaoPay = getKakaoPayInfo(memberId);

		KakaoPayDto.KakaoCancelResponse kakaoCancelResponse = cancelResponse(kakaoPay.getTid());

		cancelPay(kakaoCancelResponse.getTid());
	}

	@Transactional
	public KakaoPayDto.KakaoApproveResponse subscribeKakaoPay(Long memberId) {
		KakaoPay kakaoPay = getKakaoPayInfo(memberId);

		KakaoPayDto.KakaoApproveResponse kakaoApproveResponse = approveSubscribeResponse(kakaoPay.getSid());

		savePayInfo(memberId, kakaoApproveResponse);

		return kakaoApproveResponse;
	}

	public KakaoPayDto.KakaoSubscribeCancelResponse subscribeKakaoPayCancel(Long memberId) {
		KakaoPay kakaoPay = getKakaoPayInfo(memberId);

		KakaoPayDto.KakaoSubscribeCancelResponse kakaoSubscribeCancelResponse = subscribeCancelResponse(
			kakaoPay.getSid());

		return kakaoSubscribeCancelResponse;
	}

	public KakaoPayDto.KakaoPayStatus subcribeKakaoPayStatus(Long memberId) {
		boolean isLogExist = false;
		KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse
			= new KakaoPayDto.KakaoSubscribeStatusResponse();

		if (existsKakaoPayByMemberId(memberId)) {
			KakaoPay kakaoPay = getKakaoPayInfo(memberId);

			String sid = kakaoPay.getSid();
			if (sid != null && !sid.isEmpty()) {
				isLogExist = true;
				kakaoSubscribeStatusResponse = subscribeStatusResponse(sid);
			}
		}

		String lastApprovedAt = getLastApprovedAt(
			kakaoSubscribeStatusResponse.getCreatedAt(),
			kakaoSubscribeStatusResponse.getLastApprovedAt()
		);

		return KakaoPayDto.KakaoPayStatus.builder()
			.isLogExist(isLogExist)
			.status(kakaoSubscribeStatusResponse.getStatus())
			.lastApprovedAt(lastApprovedAt)
			.build();
	}

	public boolean getPremiumState(Long memberId) {
		boolean isPremium = false;

		if (kakaoPayRepository.existsByMember_Id(memberId)) {
			KakaoPay kakaoPay = getKakaoPayInfo(memberId);

			String sid = kakaoPay.getSid();
			if (sid != null && !sid.isEmpty()) {
				KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = subscribeStatusResponse(sid);

				if (kakaoSubscribeStatusResponse.getStatus().equals("ACTIVE")) {
					isPremium = true;
				} else {
					String lastApprovedAt = getLastApprovedAt(
						kakaoSubscribeStatusResponse.getCreatedAt(),
						kakaoSubscribeStatusResponse.getLastApprovedAt()
					);

					DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

					LocalDateTime oneMonthLater = LocalDateTime.parse(lastApprovedAt, formatter)
						.plusMonths(1).withHour(14).withMinute(0).withSecond(0);

					if (LocalDateTime.now().isBefore(oneMonthLater)) {
						isPremium = true;
					}
				}
			}
		}

		return isPremium;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		String auth = "SECRET_KEY " + secretKey;
		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-Type", "application/json");
		return httpHeaders;
	}

	private KakaoPayDto.KakaoReadyResponse kakaoPayReady(Long memberId) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("cid", cid);
		parameters.put("partner_order_id", "ORDER_ID");
		parameters.put("partner_user_id", String.valueOf(memberId));
		parameters.put("item_name", "BodyCheck 구독");
		parameters.put("quantity", "1");
		parameters.put("total_amount", "4900");
		parameters.put("vat_amount", "200");
		parameters.put("tax_free_amount", "0");
		parameters.put("approval_url",
			domain + "/payments/approve/callback"); // http://localhost:8080/payments/approve/callback
		parameters.put("fail_url", domain + "/payments/fail/callback"); // http://localhost:8080/payments/fail/callback
		parameters.put("cancel_url",
			domain + "/payments/cancel/callback"); // http://localhost:8080/payments/cancel/callback

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		return restTemplate.postForObject(
			READY_URL,
			requestEntity,
			KakaoPayDto.KakaoReadyResponse.class);
	}

	private KakaoPayDto.KakaoApproveResponse approveResponse(Long memberId, String pgToken, String tid) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("cid", cid);
		parameters.put("tid", tid);
		parameters.put("partner_order_id", "ORDER_ID");
		parameters.put("partner_user_id", String.valueOf(memberId));
		parameters.put("pg_token", pgToken);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		return restTemplate.postForObject(
			APPROVE_URL,
			requestEntity,
			KakaoPayDto.KakaoApproveResponse.class);
	}

	private KakaoPayDto.KakaoCancelResponse cancelResponse(String tid) {
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

		return restTemplate.postForObject(
			CANCEL_URL,
			requestEntity,
			KakaoPayDto.KakaoCancelResponse.class);
	}

	private KakaoPayDto.KakaoApproveResponse approveSubscribeResponse(String sid) {
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

		return restTemplate.postForObject(
			SUBSCRIBE_URL,
			requestEntity,
			KakaoPayDto.KakaoApproveResponse.class);
	}

	private KakaoPayDto.KakaoSubscribeCancelResponse subscribeCancelResponse(String sid) {
		if (sid == null || sid.isEmpty()) {
			throw new GeneralHandler(ErrorStatus.SID_NOT_EXIST);
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("cid", cid);
		parameters.put("sid", sid);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		return restTemplate.postForObject(
			SUBSCRIBE_CANCEL_URL,
			requestEntity,
			KakaoPayDto.KakaoSubscribeCancelResponse.class);
	}

	private KakaoPayDto.KakaoSubscribeStatusResponse subscribeStatusResponse(String sid) {
		if (sid == null || sid.isEmpty()) {
			throw new GeneralHandler(ErrorStatus.SID_NOT_EXIST);
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("cid", cid);
		parameters.put("sid", sid);

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		return restTemplate.postForObject(
			SUBSCRIBE_STATUS_URL,
			requestEntity,
			KakaoPayDto.KakaoSubscribeStatusResponse.class);
	}

	private KakaoPay getKakaoPayInfo(Long memberId) {
		return kakaoPayRepository.findByMember_Id(memberId)
			.orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));
	}

	private KakaoPay getKakaoPayInfoByTid(String tid) {
		return kakaoPayRepository.findByTid(tid).orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_NOT_EXIST));
	}

	private boolean existsKakaoPayByMemberId(Long memberId) {
		return kakaoPayRepository.existsByMember_Id(memberId);
	}

	private String getLastApprovedAt(String subscribeCreatedAt, String subscribeLastApprovedAt) {
		if (subscribeLastApprovedAt == null || subscribeLastApprovedAt.isEmpty()) {
			return subscribeCreatedAt;
		} else {
			return subscribeLastApprovedAt;
		}
	}

	private void saveTid(Long memberId, String tid) {
		KakaoPay kakaoPay;
		if (kakaoPayRepository.existsByMember_Id(memberId)) {
			kakaoPay = kakaoPayRepository.findByMember_Id(memberId)
				.orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
			kakaoPay.updateTid(tid);
		} else {
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
			kakaoPay = KakaoPayConverter.toKakaoPayTid(tid, member);
		}
		kakaoPayRepository.save(kakaoPay);
	}

	private void saveSid(String tid, String sid) {
		KakaoPay kakaoPay = getKakaoPayInfoByTid(tid);
		kakaoPay.updateSid(sid);

		kakaoPayRepository.save(kakaoPay);
	}

	private void savePayInfo(Long memberId, KakaoPayDto.KakaoApproveResponse kakaoApproveResponse) {
		KakaoPay kakaoPay;
		if (kakaoPayRepository.existsByMember_Id(memberId)) {
			kakaoPay = kakaoPayRepository.findByMember_Id(memberId)
				.orElseThrow(() -> new GeneralHandler(ErrorStatus.TID_SID_UNSUPPORTED));
			kakaoPay.updatePayInfo(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid());
		} else {
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
			kakaoPay = KakaoPayConverter.toKakaoPay(kakaoApproveResponse.getTid(), kakaoApproveResponse.getSid(),
				member);
		}
		kakaoPayRepository.save(kakaoPay);
	}

	private void cancelPay(String tid) {
		KakaoPay kakaoPay = getKakaoPayInfoByTid(tid);

		kakaoPayRepository.delete(kakaoPay);
	}

	@Scheduled(cron = "0 0 14 * * ?")
	public void regularPayment() {
		List<KakaoPay> kakaoPayList = kakaoPayRepository.findAllWithMemberAndSidNotNull();

		kakaoPayList.stream()
			.forEach(kakaoPay -> {
				if (kakaoPay.getSid() != null && !kakaoPay.getSid().isEmpty()) {
					KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse = subscribeStatusResponse(
						kakaoPay.getSid());

					// "ACTIVE" 상태인지 확인
					if (kakaoSubscribeStatusResponse.getStatus().equals("ACTIVE")) {
						String lastApprovedAtStr = getLastApprovedAt(
							kakaoSubscribeStatusResponse.getCreatedAt(),
							kakaoSubscribeStatusResponse.getLastApprovedAt()
						);

						// last_approved_at을 LocalDate로 변환
						DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
						LocalDate lastApprovedAt = LocalDate.parse(lastApprovedAtStr, formatter);

						LocalDate today = LocalDate.now();

						// 결제일과 오늘의 일(day)이 같고, 마지막 결제일이 이번 달이 아닌 경우에만 결제 수행
						if (today.getDayOfMonth() == lastApprovedAt.getDayOfMonth()
							&& (today.getYear() != lastApprovedAt.getYear()
							|| today.getMonthValue() != lastApprovedAt.getMonthValue())) {
							KakaoPayDto.KakaoApproveResponse approveResponse = approveSubscribeResponse(
								kakaoPay.getSid());

							savePayInfo(kakaoPay.getMember().getId(), approveResponse);
						}
						// if (today.getDayOfMonth() == lastApprovedAt.getDayOfMonth()) {
						// 	KakaoPayDto.KakaoApproveResponse approveResponse = approveSubscribeResponse(
						// 		kakaoPay.getSid());
						//
						// 	savePayInfo(kakaoPay.getMember().getId(), approveResponse);
						// }
					}
				}
			});

		//        System.out.println("정기 결제 작업 완료");
	}
}
