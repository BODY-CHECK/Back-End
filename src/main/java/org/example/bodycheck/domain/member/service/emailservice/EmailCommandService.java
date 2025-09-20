package org.example.bodycheck.domain.member.service.emailservice;

import org.example.bodycheck.domain.member.dto.emaildto.EmailRequestDto;

public interface EmailCommandService {

    void sendVerificationEmail(EmailRequestDto.EmailDto request);
    void verifyCode(EmailRequestDto.VerificationDto request);
    void sendNewPwEmail(EmailRequestDto.EmailDto request);
}
