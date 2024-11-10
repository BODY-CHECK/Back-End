package org.example.bodycheck.domain.kakao_pay.converter;

import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.member.entity.Member;

public class KakaoPayConverter {

    public static KakaoPay toKakaoPay(String tid, String sid, Member member) {
        return KakaoPay.builder()
                .member(member)
                .tid(tid)
                .sid(sid)
                .build();

    }
}
