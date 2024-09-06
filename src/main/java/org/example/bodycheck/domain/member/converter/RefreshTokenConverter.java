package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.entity.RefreshToken;

public class RefreshTokenConverter {

    public static RefreshToken toRefreshToken(String refreshToken, Member member) {
        return RefreshToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .build();

    }
}
