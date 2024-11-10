package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.kakao_pay.dto.KakaoPayDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;

public interface MemberCommandService {

    Member signUp(MemberRequestDTO.SignUpDTO request);
    JwtTokenDTO directLogin(Member member);
    JwtTokenDTO signIn(MemberRequestDTO.SignInDTO request);
    JwtTokenDTO socialLogin(String clientEmail);
    boolean isUser(String clientEmail);
    boolean isNormalUser(String clientEmail);
    void logout(Long memberId);
    JwtTokenDTO refreshToken(MemberRequestDTO.refreshTokenDTO request);
    boolean verifyPassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    String changePassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    Member findById(Long memberId);
    void savePayInfo(Long memberId, KakaoPayDTO.KakaoApproveResponse kakaoApproveResponse);
    void cancelPay(Long memberId);
}
