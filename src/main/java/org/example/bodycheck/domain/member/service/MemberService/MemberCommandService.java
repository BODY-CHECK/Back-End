package org.example.bodycheck.domain.member.service.MemberService;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.mapping.dto.RoutineDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;

import java.util.Optional;

public interface MemberCommandService {

    Member signUp(MemberRequestDTO.SignUpDTO request);
    JwtTokenDTO signIn(MemberRequestDTO.SignInDTO request);
    JwtTokenDTO socialLogin(String clientEmail);
    boolean isUser(String clientEmail);
    void logout(Long memberId);
    JwtTokenDTO refreshToken(MemberRequestDTO.refreshTokenDTO request);
    boolean verifyPassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    String changePassword(Long memberId, MemberRequestDTO.PasswordDTO request);
    Member findById(Long memberId);
}
