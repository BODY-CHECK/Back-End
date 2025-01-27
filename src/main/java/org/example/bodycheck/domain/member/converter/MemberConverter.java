package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;

public class MemberConverter {

    public static Member toMember(MemberRequestDTO.SignUpDTO request, String encodedPw) {
        return Member.builder()
                .nickname(request.getNickname())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(request.getGender())
                .email(request.getEmail())
                .pw(encodedPw)
                .exerciseType(request.getExerciseType())
                .build();
    }

    public static MemberResponseDTO.SignUpResponseDTO toSignUpResponseDTO(Member member) {
        return MemberResponseDTO.SignUpResponseDTO.builder()
                .email(member.getEmail())
                .build();
    }

    public static MemberResponseDTO.AccessTokenResponseDTO toAccessTokenResponseDTO(JwtTokenDTO jwtTokenDTO) {
        return MemberResponseDTO.AccessTokenResponseDTO.builder()
                .accessToken(jwtTokenDTO.getAccessToken())
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();
    }

    public static MemberResponseDTO.SocialLoginLocationResponseDTO toSocialLoginLocationResponseDTO(String locationKakao, String locationGoogle) {
        return MemberResponseDTO.SocialLoginLocationResponseDTO.builder()
                .locationKakao(locationKakao)
                .locationGoogle(locationGoogle)
                .build();
    }

    public static MemberResponseDTO.SocialLoginResponseDTO toSocialLoginResponseDTO(boolean isUser, String email, String nickname, JwtTokenDTO jwtTokenDTO) {
        String accessToken;
        String refreshToken;
        if (jwtTokenDTO == null) {
            accessToken = null;
            refreshToken = null;
        }
        else {
            accessToken = jwtTokenDTO.getAccessToken();
            refreshToken = jwtTokenDTO.getRefreshToken();
        }
        return MemberResponseDTO.SocialLoginResponseDTO.builder()
                .isUser(isUser)
                .email(email)
                .nickname(nickname)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static MemberResponseDTO.MyPageResponseDTO toMyPageResponseDTO(Member member, boolean isPremium) {
        return MemberResponseDTO.MyPageResponseDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .height(member.getHeight())
                .weight(member.getWeight())
                .exerciseType(member.getExerciseType())
                .premium(isPremium)
                .build();
    }
}
