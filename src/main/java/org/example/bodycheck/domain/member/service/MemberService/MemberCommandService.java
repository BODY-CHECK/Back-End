package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;

public interface MemberCommandService {

    Member signUp(MemberRequestDTO.SignUpDTO request);
    void deactivate(Member member, String accessToken);
    JwtTokenDTO directLogin(Member member);
    JwtTokenDTO signIn(MemberRequestDTO.SignInDTO request);
    MemberResponseDTO.SocialLoginResponseDTO handleSocialLogin(String clientEmail, String nickname);
    void logout(String clientEmail, String accessToken);
    JwtTokenDTO refreshToken(MemberRequestDTO.refreshTokenDTO request);
    boolean verifyPassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    String changePassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    Member findById(Long memberId);
}
