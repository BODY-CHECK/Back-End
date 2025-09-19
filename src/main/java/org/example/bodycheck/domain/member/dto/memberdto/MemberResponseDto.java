package org.example.bodycheck.domain.member.dto.memberdto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;
import org.example.bodycheck.domain.enums.LoginType;

public class MemberResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpResponseDto {
        @Email
        private String email;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessTokenResponseDto {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLoginLocationResponseDto {
        private String locationKakao;
        private String locationGoogle;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLoginResponseDto {
        private boolean isUser;
        @Email
        private String email;
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyPageResponseDto {
        private String email;
        private String nickname;
        private Gender gender;
        private Float height;
        private Float weight;
        private ExerciseType exerciseType;
        private LoginType loginType;
        private boolean premium;
    }
}
