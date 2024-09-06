package org.example.bodycheck.domain.member.dto.MemberDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class SignUpDTO {
        private String nickname;
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        @Size(min = 8, message = "비밀번호는 최소 4자 이상이어야 합니다.")
        private String pw;
    }

    @Getter
    public static class SignInDTO {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        private String pw;
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
