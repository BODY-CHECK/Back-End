package org.example.bodycheck.domain.member.dto.MemberDTO;

import jakarta.validation.constraints.Email;
import lombok.*;

public class MemberResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpResponseDTO {
        @Email
        private String email;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessTokenResponseDTO {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLoginLocationResponseDTO {
        private String locationKakao;
        private String locationGoogle;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLoginResponseDTO {
        private boolean isUser;
        @Email
        private String email;
        private String nickname;
        private String accessToken;
        private String refreshToken;
    }
}
