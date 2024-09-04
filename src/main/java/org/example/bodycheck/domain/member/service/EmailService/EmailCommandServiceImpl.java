package org.example.bodycheck.domain.member.service.EmailService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.member.converter.EmailConverter;
import org.example.bodycheck.domain.member.entity.Email;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.EmailRepository;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.domain.member.dto.EmailDTO.EmailRequestDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailCommandServiceImpl implements EmailCommandService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendVerificationEmail(EmailRequestDTO.EmailDTO request) {
        String code = generateVerificationCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("[Body-Check] 인증 코드");
        message.setText("당신의 인증 코드는: " + code + "입니다.");

        mailSender.send(message);

        Email mail = EmailConverter.toMail(request.getEmail(), code);

        emailRepository.save(mail);
    }

    @Override
    @Transactional
    public boolean verifyCode(EmailRequestDTO.VerificationDTO request) {
        Email mail = emailRepository.findByEmail(request.getEmail()).orElseThrow(() -> new GeneralHandler(ErrorStatus.VERIFICATION_CODE_NOT_EXIST));
        String storedCode = mail.getCode();
        return storedCode.equals(request.getCode());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999); // 6자리 숫자 생성
        return String.format("%06d", code);
    }

    @Override
    @Transactional
    public void sendNewPwEmail(EmailRequestDTO.EmailDTO request) {
        String newPw = generateNewPw();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("[Body-Check] 임시 비밀번호");
        message.setText("당신의 임시 비밀번호는: " + newPw + "입니다.");

        mailSender.send(message);

        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setPw(passwordEncoder.encode(newPw));
        memberRepository.save(member);
    }

    private String generateNewPw() {
        Random random = new Random();
        int code = random.nextInt(99999999); // 8자리 숫자 생성
        return String.format("%08d", code);
    }
}
