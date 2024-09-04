package org.example.bodycheck.domain.member.service.EmailService;

import org.example.bodycheck.domain.member.dto.EmailDTO.EmailRequestDTO;

public interface EmailCommandService {

    void sendVerificationEmail(EmailRequestDTO.EmailDTO request);
    boolean verifyCode(EmailRequestDTO.VerificationDTO request);
    void sendNewPwEmail(EmailRequestDTO.EmailDTO request);

}
