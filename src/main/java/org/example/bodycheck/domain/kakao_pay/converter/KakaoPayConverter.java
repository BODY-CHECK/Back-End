package org.example.bodycheck.domain.kakao_pay.converter;

import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.member.entity.Member;

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

    public static KakaoPayDTO.KakaoPayStatus toKakaoPayStatus(boolean isLogExist, KakaoPayDTO.KakaoSubscribeStatusResponse kakaoSubscribeStatusResponse) {
        String status = kakaoSubscribeStatusResponse.getStatus();

        String last_approved_at;
        if(kakaoSubscribeStatusResponse.getLast_approved_at() == null || kakaoSubscribeStatusResponse.getLast_approved_at().isEmpty()) {
            last_approved_at = kakaoSubscribeStatusResponse.getCreated_at();
        }
        else last_approved_at = kakaoSubscribeStatusResponse.getLast_approved_at();

        return KakaoPayDTO.KakaoPayStatus.builder()
                .isLogExist(isLogExist)
                .status(status)
                .last_approved_at(last_approved_at)
                .build();
    }
}
