package org.example.bodycheck.external.kakao.pay.converter;

import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.external.kakao.pay.dto.KakaoPayDto;
import org.example.bodycheck.external.kakao.pay.entity.KakaoPay;

public class KakaoPayConverter {

	public static KakaoPay toKakaoPayTid(String tid, Member member) {
		return KakaoPay.builder()
			.member(member)
			.tid(tid)
			.build();
	}

	public static KakaoPay toKakaoPay(String tid, String sid, Member member) {
		return KakaoPay.builder()
			.member(member)
			.tid(tid)
			.sid(sid)
			.build();
	}

	public static KakaoPayDto.KakaoPayStatus toKakaoPayStatus(boolean isLogExist,
		KakaoPayDto.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse) {
		String status = kakaoSubscribeStatusResponse.getStatus();

		String lastApprovedAt;
		if (kakaoSubscribeStatusResponse.getLastApprovedAt() == null
			|| kakaoSubscribeStatusResponse.getLastApprovedAt().isEmpty()) {
			lastApprovedAt = kakaoSubscribeStatusResponse.getCreatedAt();
		} else {
			lastApprovedAt = kakaoSubscribeStatusResponse.getLastApprovedAt();
		}

		return KakaoPayDto.KakaoPayStatus.builder()
			.isLogExist(isLogExist)
			.status(status)
			.lastApprovedAt(lastApprovedAt)
			.build();
	}
}
