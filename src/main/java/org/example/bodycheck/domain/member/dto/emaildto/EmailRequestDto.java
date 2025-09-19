package org.example.bodycheck.domain.member.dto.emaildto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

public class EmailRequestDto {

    @Getter
    public static class EmailDto {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
    }

    @Getter
    public static class VerificationDto {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        private String code;
    }
}
