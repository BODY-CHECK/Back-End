package org.example.bodycheck.common.jwt;

import java.io.IOException;
import java.util.Base64;

import org.example.bodycheck.external.oauth2.apple.dto.AppleLoginDto;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AppleTokenDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public AppleLoginDto.AppleUserInfoResponseDto decodePayload(String idToken) {
		try {
			String[] parts = idToken.split("\\.");
			if (parts.length != 3) {
				throw new IllegalArgumentException("Invalid JWT");
			}

			String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
			// DTO 매핑
			return objectMapper.readValue(payloadJson, AppleLoginDto.AppleUserInfoResponseDto.class);

		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid JWT format", e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to map payload to DTO", e);
		}
	}
}
