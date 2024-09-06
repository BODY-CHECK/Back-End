package org.example.bodycheck.domain.member.dto.EmailDTO;

import jakarta.validation.constraints.Email;
import lombok.Getter;

public class EmailRequestDTO {

    @Getter
    public static class EmailDTO {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
    }

    @Getter
    public static class VerificationDTO {
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        private String email;
        private String code;
    }
}
