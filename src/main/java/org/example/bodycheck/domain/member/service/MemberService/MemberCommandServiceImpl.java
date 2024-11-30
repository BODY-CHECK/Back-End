package org.example.bodycheck.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.common.jwt.JwtTokenProvider;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.kakao_pay.controller.KakaoPayController;
import org.example.bodycheck.domain.kakao_pay.converter.KakaoPayConverter;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.kakao_pay.entity.KakaoPay;
import org.example.bodycheck.domain.kakao_pay.repository.KakaoPayRepository;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.converter.RefreshTokenConverter;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.entity.RefreshToken;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;
import org.example.bodycheck.domain.member.repository.RefreshRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoPayRepository kakaoPayRepository;

    @Override
    @Transactional
    public Member signUp(MemberRequestDTO.SignUpDTO request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new GeneralHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new GeneralHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
        }

        String pw;
        if (request.getPw() == null || request.getPw().isEmpty()) {
            pw = null;
        } else {
            pw = passwordEncoder.encode(request.getPw());
        }

        Member member = MemberConverter.toMember(request, pw);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public JwtTokenDTO directLogin(Member member) {
        String clientEmail = member.getEmail();

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        RefreshToken refreshToken;
        if (refreshRepository.existsByMember_Id(member.getId())) {
            refreshToken = refreshRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_UNSUPPORTED));
            refreshToken.setRefreshToken(jwtTokenDTO.getRefreshToken());
        }
        else {
            refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
        }
        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public JwtTokenDTO signIn(MemberRequestDTO.SignInDTO request) {
        String clientEmail = request.getEmail();
        String clientPw = request.getPw();

        Member member = memberRepository.findByEmail(clientEmail).orElseThrow(() -> new GeneralHandler(ErrorStatus.LOGIN_UNAUTHORIZED));

        if (!passwordEncoder.matches(clientPw, member.getPw())) {
            throw new GeneralHandler(ErrorStatus.LOGIN_UNAUTHORIZED);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        RefreshToken refreshToken;
        if (refreshRepository.existsByMember_Id(member.getId())) {
            refreshToken = refreshRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_UNSUPPORTED));
            refreshToken.setRefreshToken(jwtTokenDTO.getRefreshToken());
        }
        else {
            refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
        }
        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public JwtTokenDTO socialLogin(String clientEmail) {

        Member member = memberRepository.findByEmail(clientEmail).orElseThrow(() -> new GeneralHandler(ErrorStatus.LOGIN_UNAUTHORIZED));

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        RefreshToken refreshToken;
        if (refreshRepository.existsByMember_Id(member.getId())) {
            refreshToken = refreshRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_UNSUPPORTED));
            refreshToken.setRefreshToken(jwtTokenDTO.getRefreshToken());
        }
        else {
            refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
        }
        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public boolean isUser(String clientEmail) {

        boolean isUser = memberRepository.existsByEmail(clientEmail);

        return isUser;
    }

    @Override
    @Transactional
    public boolean isNormalUser(String clientEmail) {

        Member member = memberRepository.findByEmail(clientEmail).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (member.getPw() == null || member.getPw().isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    @Transactional
    public void logout(Long memberId) {
        RefreshToken deleteRefreshToken = refreshRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST));
        refreshRepository.delete(deleteRefreshToken);
        refreshRepository.flush();
    }

    @Override
    @Transactional
    public JwtTokenDTO refreshToken(MemberRequestDTO.refreshTokenDTO request) {
        String token = request.getRefreshToken();

        if (!refreshRepository.existsByRefreshToken(token) || !jwtTokenProvider.validateToken(token)) {
            throw new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST);
        }

        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(token);
        RefreshToken deleteRefreshToken = refreshRepository.findByRefreshToken(token).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST));
        refreshRepository.delete(deleteRefreshToken);

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        RefreshToken refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public boolean verifyPassword(Long memberId, MemberRequestDTO.PasswordDTO request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getPw() == null || member.getPw().isEmpty()) {
            throw new GeneralHandler(ErrorStatus.KAKAO_USER);
        }

        String clientPw = request.getPw();
        return passwordEncoder.matches(clientPw, member.getPw());
    }

    @Override
    @Transactional
    public String changePassword(Long memberId, MemberRequestDTO.PasswordDTO request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setPw(passwordEncoder.encode(request.getPw()));
        memberRepository.save(member);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
