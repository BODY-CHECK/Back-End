package org.example.bodycheck.domain.member.dto.memberdto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;
import org.example.bodycheck.domain.enums.LoginType;

public class MemberRequestDto {

    @Getter
    public static class SignUpDto {
        private String nickname;
        private Float height;
        private Float weight;
        private Gender gender;
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@$%^&*])[a-zA-Z0-9!@$%^&*]{8,}",
                //message = "비밀번호는 영문, 숫자, 특수문자 '!,@,$,%,^,&,*' 를 포함해야 하며, 최소 8자 이상이어야 합니다.")
        private String pw;
        private ExerciseType exerciseType;
        private LoginType loginType;
    }

    @Getter
    public static class SignInDto {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        private String pw;
        private LoginType loginType;
    }

    @Getter
    public static class SocialLoginDto {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
    }

    @Getter
    public static class PasswordDto {
        private String pw;
    }

    @Getter
    public static class accessTokenDto {
        private String accessToken;
    }

    @Getter
    public static class refreshTokenDto {
        private String refreshToken;
    }
}
