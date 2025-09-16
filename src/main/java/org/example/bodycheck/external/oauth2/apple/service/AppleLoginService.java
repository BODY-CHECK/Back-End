package org.example.bodycheck.external.oauth2.apple.service;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.AppleTokenDecoder;
import org.example.bodycheck.common.jwt.JwtParser;
import org.example.bodycheck.external.oauth2.apple.dto.AppleLoginDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppleLoginService {

    private final AppleTokenDecoder appleTokenDecoder;
    private final JwtParser jwtParser;

    public AppleLoginDto.AppleUserInfoResponseDto loginWithApple(String accessToken) {
        AppleLoginDto.AppleUserInfoResponseDto appleUserInfoResponseDto = appleTokenDecoder.decodePayload(accessToken);

        return appleUserInfoResponseDto;
    }
}
