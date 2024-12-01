package org.example.bodycheck.domain.member.dto.MemberDTO;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;

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

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyPageResponseDTO {
        private String email;

        private String nickname;

        private Gender gender;

        private Float height;

        private Float weight;

        private ExerciseType exerciseType;

        private boolean premium;
    }
}
