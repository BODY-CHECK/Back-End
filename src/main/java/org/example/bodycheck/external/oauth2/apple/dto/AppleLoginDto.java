package org.example.bodycheck.external.oauth2.apple.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppleLoginDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AppleUserInfoResponseDto {
		@JsonProperty("iss")
		private String iss;
		@JsonProperty("aud")
		private String aud;
		@JsonProperty("exp")
		private Long exp;
		@JsonProperty("iat")
		private Long iat;
		@JsonProperty("sub")
		private String sub;
		@JsonProperty("nonce")
		private String nonce;
		@JsonProperty("c_hash")
		private String cHash;
		@JsonProperty("email")
		private String email;
		@JsonProperty("email_verified")
		private Boolean emailVerified;
		@JsonProperty("is_private_email")
		private Boolean isPrivateEmail;
		@JsonProperty("auth_time")
		private Long authTime;
		@JsonProperty("nonce_supported")
		private Boolean nonceSupported;
		@JsonProperty("real_user_status")
		private Integer realUserStatus;
	}
}
