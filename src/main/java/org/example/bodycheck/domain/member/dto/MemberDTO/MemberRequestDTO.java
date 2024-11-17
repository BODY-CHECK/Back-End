package org.example.bodycheck.domain.member.dto.MemberDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.bodycheck.domain.enums.ExerciseType;
import org.example.bodycheck.domain.enums.Gender;

public class MemberRequestDTO {

    @Getter
    public static class SignUpDTO {
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
    }

    @Getter
    public static class SignInDTO {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        private String pw;
    }

    @Getter
    public static class SocialLoginDTO {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
    }

    @Getter
    public static class PasswordDTO {
        private String pw;
    }

    @Getter
    public static class refreshTokenDTO {
        private String refreshToken;
    }
}
