package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;

public class MemberConverter {

    public static Member toMember(MemberRequestDTO.SignUpDTO request, String encodedPw) {
        return Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .pw(encodedPw)
                .build();
    }

    public static MemberResponseDTO.SignUpResponseDTO toSignUpResponseDTO(Member member) {
        return MemberResponseDTO.SignUpResponseDTO.builder()
                .email(member.getEmail())
                .build();
    }

    public static MemberResponseDTO.AccessTokenResponseDTO accessTokenResponseDTO(JwtTokenDTO jwtTokenDTO) {
        return MemberResponseDTO.AccessTokenResponseDTO.builder()
                .accessToken(jwtTokenDTO.getAccessToken())
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();
    }
}
