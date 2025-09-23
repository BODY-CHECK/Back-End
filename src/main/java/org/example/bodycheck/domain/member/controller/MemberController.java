package org.example.bodycheck.domain.member.controller;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.memberservice.MemberCommandService;
import org.example.bodycheck.domain.member.service.memberservice.SettingService;
import org.example.bodycheck.external.kakao.pay.service.KakaoPayService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {

	private final MemberCommandService memberCommandService;
	private final SettingService settingService;
	private final KakaoPayService kakaoPayService;

	@PostMapping("/email/sign-up")
	@Operation(summary = "회원가입 API", description = "이메일로 회원가입을 하는 API 입니다.")
	public ApiResponse<MemberResponseDto.AccessTokenResponseDto> signUp(
		@Valid @RequestBody MemberRequestDto.SignUpDto request) {
		JwtTokenDto jwtTokenDto = memberCommandService.signUp(request);
		return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDto(jwtTokenDto));
	}

	@PostMapping("/deactivate")
	@Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴하는 API 입니다.")
	public ApiResponse<String> deactivate(@AuthUser Member member,
		@RequestHeader("Authorization") String authorizationHeader) {
		String token =
			authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

		memberCommandService.deactivate(member, token);
		return ApiResponse.onSuccess("OK");
	}

	@PostMapping("/email/sign-in")  // JWT 토큰을 생성하여 반환
	@Operation(summary = "로그인 API", description = "로그인을 하는 API 입니다.")
	public ApiResponse<MemberResponseDto.AccessTokenResponseDto> signIn(
		@RequestBody MemberRequestDto.SignInDto request) {
		JwtTokenDto jwtTokenDto = memberCommandService.signIn(request);
		return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDto(jwtTokenDto));
	}

	//    @PostMapping("/social-login")  // JWT 토큰을 생성하여 반환
	//    @Operation(summary = "소셜 로그인 API", description = "소셜로 로그인을 하는 API 입니다.")
	//    public ApiResponse<MemberResponseDto.AccessTokenResponseDto> socialLogin(
	//    @RequestBody MemberRequestDto.SocialLoginDto request) {
	//        JwtTokenDto jwtTokenDTO = memberCommandService.socialLogin(request.getEmail());
	//        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDto(jwtTokenDTO));
	//    }

	@PostMapping("/logout")
	@Operation(summary = "로그아웃 API", description = "로그아웃을 하는 API 입니다.")
	public ApiResponse<String> logout(@AuthUser Member member,
		@RequestHeader("Authorization") String authorizationHeader) {
		String token =
			authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

		memberCommandService.logout(member.getEmail(), token);
		return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
	}

	@PostMapping("/refresh-token")
	@Operation(summary = "리프레시 토큰으로 인가를 확인하는 API", description = "리프레시 토큰으로 인가를 확인하는 API 입니다.")
	public ApiResponse<MemberResponseDto.AccessTokenResponseDto> refreshToken(
		@RequestBody MemberRequestDto.RefreshTokenDto request) {
		JwtTokenDto jwtTokenDto = memberCommandService.refreshToken(request);
		return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDto(jwtTokenDto));
	}

	@PostMapping("/verify-password")
	@Operation(summary = "비밀번호 검증 API", description = "비밀번호를 이용하여 사용자 본인이 맞는지 확인하는 API 입니다.")
	public ApiResponse<String> verifyPassword(@AuthUser Member member,
		@RequestBody MemberRequestDto.PasswordDto request) {
		memberCommandService.verifyPassword(member.getId(), request);
		return ApiResponse.onSuccess("비밀번호 인증이 완료되었습니다.");
	}

	@PutMapping("/change-password")
	@Operation(summary = "비밀번호 변경 API", description = "비밀번호를 변경하는 API 입니다.")
	public ApiResponse<String> changePassword(@AuthUser Member member,
		@RequestBody MemberRequestDto.PasswordDto request) {
		memberCommandService.changePassword(member.getId(), request);
		return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
	}

	@GetMapping("/my-page")  // JWT 토큰을 생성하여 반환
	@Operation(summary = "마이페이지 조회 API", description = "마이페이지 정보를 조회하는 API 입니다.")
	public ApiResponse<MemberResponseDto.MyPageResponseDto> myPage(@AuthUser Member member) {
		boolean isPremium = kakaoPayService.getPremiumState(member.getId());
		return ApiResponse.onSuccess(MemberConverter.toMyPageResponseDto(member, isPremium));
	}

	@PostMapping("/setting/profile")
	@Operation(summary = "프로필 변경 API")
	public ApiResponse<MemberResponseDto.MemberSettingDto> profileSetting(@AuthUser Member member,
		@RequestBody MemberRequestDto.MemberProfileSettingDto request) {
		return ApiResponse.onSuccess(settingService.profileSetting(member, request));
	}
}
