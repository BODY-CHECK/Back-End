package org.example.bodycheck.external.oauth2.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppleLoginDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppleUserInfoResponseDto {
        private String iss;
        private String aud;
        private Long exp;
        private Long iat;
        private String sub;
        private String nonce;
        private String c_hash;
        private String email;
        private Boolean email_verified;
        private Long auth_time;
        private Boolean nonce_supported;
    }
}
