package org.example.bodycheck.external.oauth2.apple.dto;

import lombok.Builder;
import lombok.Getter;

public class AppleLoginDto {

    @Builder
    @Getter
    public static class AppleUserInfoResponseDto {
        public String email;
    }
}
