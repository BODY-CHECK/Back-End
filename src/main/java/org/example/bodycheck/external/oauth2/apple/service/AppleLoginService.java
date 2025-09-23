package org.example.bodycheck.external.oauth2.apple.service;

import org.example.bodycheck.common.jwt.AppleTokenDecoder;
import org.example.bodycheck.external.oauth2.apple.dto.AppleLoginDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleLoginService {

	private final AppleTokenDecoder appleTokenDecoder;

	public AppleLoginDto.AppleUserInfoResponseDto loginWithApple(String accessToken) {
		AppleLoginDto.AppleUserInfoResponseDto appleUserInfoResponseDto = appleTokenDecoder.decodePayload(accessToken);

		return appleUserInfoResponseDto;
	}
}
