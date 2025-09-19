package org.example.bodycheck.domain.member.service.memberservice;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.common.jwt.JwtTokenProvider;
import org.example.bodycheck.common.apiPayload.code.status.ErrorStatus;
import org.example.bodycheck.common.exception.handler.GeneralHandler;
import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.external.redis.service.RedisService;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
//    private final RefreshRepository refreshRepository;
    private final MemberQueryService memberQueryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    @Transactional
    public Member signUp(MemberRequestDto.SignUpDto request) {
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
    public void deactivate(Member member, String accessToken) {
        member.deactivate(LocalDate.now());
        memberRepository.save(member);

        long ttl = jwtTokenProvider.getExpiration(accessToken);
        redisService.saveKeyValueWithTTL("blacklist:" + accessToken, "logout", ttl);
        redisService.deleteValue("refresh:" + member.getEmail());
    }

    @Override
    @Transactional
    public JwtTokenDto directLogin(Member member) {
        String clientEmail = member.getEmail();

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDto jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        redisService.saveKeyValueWithTTL("refresh:" + clientEmail, jwtTokenDTO.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME);

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        RefreshToken refreshToken;
//        if (refreshRepository.existsByMember_Id(member.getId())) {
//            refreshToken = refreshRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_UNSUPPORTED));
//            refreshToken.setRefreshToken(jwtTokenDTO.getRefreshToken());
//        }
//        else {
//            refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
//        }
//        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public JwtTokenDto signIn(MemberRequestDto.SignInDto request) {
        String clientEmail = request.getEmail();
        String clientPw = request.getPw();

        Member member = memberRepository.findByEmail(clientEmail).orElseThrow(() -> new GeneralHandler(ErrorStatus.LOGIN_UNAUTHORIZED));

        if (member.getInactiveDate() != null) {
            throw new GeneralHandler(ErrorStatus.MEMBER_DEACTIVATED);
        }

//        if (member.getLoginType() != request.getLoginType()) {
//            throw new GeneralHandler(ErrorStatus.SOCIAL_ACCOUNT_ALREADY_REGISTERED);
//        }

        if (!passwordEncoder.matches(clientPw, member.getPw())) {
            throw new GeneralHandler(ErrorStatus.LOGIN_UNAUTHORIZED);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDto jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        redisService.saveKeyValueWithTTL("refresh:" + clientEmail, jwtTokenDTO.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME);

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        RefreshToken refreshToken;
//        if (refreshRepository.existsByMember_Id(member.getId())) {
//            refreshToken = refreshRepository.findByMember_Id(member.getId()).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_UNSUPPORTED));
//            refreshToken.setRefreshToken(jwtTokenDTO.getRefreshToken());
//        }
//        else {
//            refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
//        }
//        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public MemberResponseDto.SocialLoginResponseDto handleSocialLogin(String clientEmail, LoginType loginType) {
        Optional<Member> optionalMember = memberRepository.findByEmail(clientEmail);

        if (optionalMember.isEmpty()) {
            return MemberResponseDto.SocialLoginResponseDto.builder()
                    .isUser(false)
                    .email(clientEmail)
                    .accessToken(null)
                    .refreshToken(null)
                    .build();
        }

        Member member = optionalMember.get();
        if (memberQueryService.isRegisteredWithEmail(member) || !memberQueryService.isRegisteredWithSocial(member, loginType)) {
            throw new GeneralHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        if (member.getInactiveDate() != null) {
            throw new GeneralHandler(ErrorStatus.MEMBER_DEACTIVATED);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(clientEmail, null);

        JwtTokenDto jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        redisService.saveKeyValueWithTTL("refresh:" + clientEmail, jwtTokenDTO.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME);

        return MemberResponseDto.SocialLoginResponseDto.builder()
                .isUser(true)
                .email(clientEmail)
                .accessToken(jwtTokenDTO.getAccessToken())
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();
    }

    @Override
    @Transactional
    public void logout(String clientEmail, String accessToken) {
        long ttl = jwtTokenProvider.getExpiration(accessToken);
        redisService.saveKeyValueWithTTL("blacklist:" + accessToken, "logout", ttl);
        redisService.deleteValue("refresh:" + clientEmail);

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        RefreshToken deleteRefreshToken = refreshRepository.findByMember_Id(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST));
//        refreshRepository.delete(deleteRefreshToken);
//        refreshRepository.flush();
    }

    @Override
    @Transactional
    public JwtTokenDto refreshToken(MemberRequestDto.refreshTokenDto request) {
        String token = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(token)) {
            throw new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST);
        }

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        if (!refreshRepository.existsByRefreshToken(token) || !jwtTokenProvider.validateToken(token)) {
//            throw new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST);
//        }

        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(token);
        String clientEmail = authentication.getName();

        if (!redisService.existKey("refresh:" + clientEmail) || !token.equals(redisService.getValues("refresh:" + clientEmail))) {
            throw new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST);
        }

        redisService.deleteValue("refresh:" + clientEmail);

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        RefreshToken deleteRefreshToken = refreshRepository.findByRefreshToken(token).orElseThrow(() -> new GeneralHandler(ErrorStatus.TOKEN_NOT_EXIST));
//        refreshRepository.delete(deleteRefreshToken);

        JwtTokenDto jwtTokenDTO = jwtTokenProvider.generateTokenDTO(authentication);

        redisService.saveKeyValueWithTTL("refresh:" + clientEmail, jwtTokenDTO.getRefreshToken(), JwtTokenProvider.REFRESH_TOKEN_EXPIRE_TIME);

//        // 이전 로직 - 리프레시 토큰을 DB에 저장 할 경우
//        String email = authentication.getName();
//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
//
//        RefreshToken refreshToken = RefreshTokenConverter.toRefreshToken(jwtTokenDTO.getRefreshToken(), member);
//        refreshRepository.save(refreshToken);

        return jwtTokenDTO;
    }

    @Override
    @Transactional
    public boolean verifyPassword(Long memberId, MemberRequestDto.PasswordDto request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getPw() == null || member.getPw().isEmpty()) {
            throw new GeneralHandler(ErrorStatus.KAKAO_USER);
        }

        String clientPw = request.getPw();
        return passwordEncoder.matches(clientPw, member.getPw());
    }

    @Override
    @Transactional
    public String changePassword(Long memberId, MemberRequestDto.PasswordDto request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.updatePw(passwordEncoder.encode(request.getPw()));
        memberRepository.save(member);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
