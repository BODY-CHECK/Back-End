package org.example.bodycheck.domain.member.converter;

import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;

public class MemberConverter {

    public static Member toMember(MemberRequestDto.SignUpDto request, String encodedPw) {
        return Member.builder()
                .nickname(request.getNickname())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(request.getGender())
                .email(request.getEmail())
                .pw(encodedPw)
                .exerciseType(request.getExerciseType())
                .loginType(request.getLoginType())
                .build();
    }

    public static MemberResponseDto.SignUpResponseDto toSignUpResponseDTO(Member member) {
        return MemberResponseDto.SignUpResponseDto.builder()
                .email(member.getEmail())
                .build();
    }

    public static MemberResponseDto.AccessTokenResponseDto toAccessTokenResponseDTO(JwtTokenDto jwtTokenDTO) {
        return MemberResponseDto.AccessTokenResponseDto.builder()
                .accessToken(jwtTokenDTO.getAccessToken())
                .refreshToken(jwtTokenDTO.getRefreshToken())
                .build();
    }

    public static MemberResponseDto.SocialLoginLocationResponseDto toSocialLoginLocationResponseDTO(String locationKakao, String locationGoogle) {
        return MemberResponseDto.SocialLoginLocationResponseDto.builder()
                .locationKakao(locationKakao)
                .locationGoogle(locationGoogle)
                .build();
    }

    public static MemberResponseDto.SocialLoginResponseDto toSocialLoginResponseDTO(boolean isUser, String email, String nickname, JwtTokenDto jwtTokenDTO) {
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
        return MemberResponseDto.SocialLoginResponseDto.builder()
                .isUser(isUser)
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static MemberResponseDto.MyPageResponseDto toMyPageResponseDTO(Member member, boolean isPremium) {
        return MemberResponseDto.MyPageResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .height(member.getHeight())
                .weight(member.getWeight())
                .exerciseType(member.getExerciseType())
                .loginType(member.getLoginType())
                .premium(isPremium)
                .build();
    }
}
