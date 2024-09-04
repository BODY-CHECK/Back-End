package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;

public interface MemberCommandService {

    Member signUp(MemberRequestDTO.SignUpDTO request);
    JwtTokenDTO signIn(MemberRequestDTO.SignInDTO request);
    void logout(Long memberId);
    JwtTokenDTO refreshToken(MemberRequestDTO.refreshTokenDTO request);
    boolean verifyPassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    String changePassword(Long memberId, MemberRequestDTO.PasswordDTO request);
}
