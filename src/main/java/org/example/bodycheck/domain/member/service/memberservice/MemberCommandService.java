package org.example.bodycheck.domain.member.service.memberservice;

import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;

public interface MemberCommandService {

    Member signUp(MemberRequestDto.SignUpDto request);
    void deactivate(Member member, String accessToken);
    JwtTokenDto directLogin(Member member);
    JwtTokenDto signIn(MemberRequestDto.SignInDto request);
    MemberResponseDto.SocialLoginResponseDto handleSocialLogin(String clientEmail, LoginType loginType);
    void logout(String clientEmail, String accessToken);
    JwtTokenDto refreshToken(MemberRequestDto.refreshTokenDto request);
    boolean verifyPassword(Long memberId, MemberRequestDto.PasswordDto request);
    String changePassword(Long memberId, MemberRequestDto.PasswordDto request);
    Member findById(Long memberId);
}
