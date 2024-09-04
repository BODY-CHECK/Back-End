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
}
