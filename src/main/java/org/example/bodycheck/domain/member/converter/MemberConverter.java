package org.example.bodycheck.domain.member.converter;

import java.time.LocalDate;

import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;

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

	public static MemberResponseDto.SignUpResponseDto toSignUpResponseDto(Member member) {
		return MemberResponseDto.SignUpResponseDto.builder()
			.email(member.getEmail())
			.build();
	}

	public static MemberResponseDto.AccessTokenResponseDto toAccessTokenResponseDto(JwtTokenDto jwtTokenDto) {
		return MemberResponseDto.AccessTokenResponseDto.builder()
			.accessToken(jwtTokenDto.getAccessToken())
			.refreshToken(jwtTokenDto.getRefreshToken())
			.build();
	}

	public static MemberResponseDto.SocialLoginLocationResponseDto toSocialLoginLocationResponseDto(
		String locationKakao, String locationGoogle) {
		return MemberResponseDto.SocialLoginLocationResponseDto.builder()
			.locationKakao(locationKakao)
			.locationGoogle(locationGoogle)
			.build();
	}

	public static MemberResponseDto.SocialLoginResponseDto toSocialLoginResponseDto(boolean isUser, String email,
		String nickname, JwtTokenDto jwtTokenDto) {
		String accessToken;
		String refreshToken;
		if (jwtTokenDto == null) {
			accessToken = null;
			refreshToken = null;
		} else {
			accessToken = jwtTokenDto.getAccessToken();
			refreshToken = jwtTokenDto.getRefreshToken();
		}
		return MemberResponseDto.SocialLoginResponseDto.builder()
			.isUser(isUser)
			.email(email)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public static MemberResponseDto.MyPageResponseDto toMyPageResponseDto(Member member, boolean isPremium) {
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
