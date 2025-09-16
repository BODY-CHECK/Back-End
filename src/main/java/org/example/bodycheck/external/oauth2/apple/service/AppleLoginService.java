package org.example.bodycheck.external.oauth2.apple.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtParser;
import org.example.bodycheck.external.oauth2.apple.dto.AppleLoginDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppleLoginService {

    private final JwtParser jwtParser;

    public AppleLoginDto.AppleUserInfoResponseDto loginWithApple(String accessToken) {
        String email = jwtParser.getClaim(accessToken, "email", String.class);

        return AppleLoginDto.AppleUserInfoResponseDto.builder()
                .email(email)
                .build();
    }
}
