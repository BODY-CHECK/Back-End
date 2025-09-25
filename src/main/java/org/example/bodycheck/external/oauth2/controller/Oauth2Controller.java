package org.example.bodycheck.external.oauth2.controller;

import org.example.bodycheck.common.apipayload.ApiResponse;
import org.example.bodycheck.domain.enums.LoginType;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.domain.member.service.memberservice.MemberCommandService;
import org.example.bodycheck.external.oauth2.apple.dto.AppleLoginDto;
import org.example.bodycheck.external.oauth2.apple.service.AppleLoginService;
import org.example.bodycheck.external.oauth2.google.dto.GoogleLoginDto;
import org.example.bodycheck.external.oauth2.google.service.GoogleLoginService;
import org.example.bodycheck.external.oauth2.kakao.dto.KakaoLoginDto;
import org.example.bodycheck.external.oauth2.kakao.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/login/oauth2")
public class Oauth2Controller {

	private final KakaoLoginService kakaoLoginService;
	private final GoogleLoginService googleLoginService;
	private final MemberCommandService memberCommandService;
	private final AppleLoginService appleLoginService;

	@Value("${spring.kakao.client_id}")
	private String clientIdKakao;

	@Value("${spring.kakao.redirect_uri}")
	private String redirectUriKakao;

	@Value("${spring.google.client_id}")
	private String clientIdGoogle;

	@Value("${spring.google.redirect_uri}")
	private String redirectUriGoogle;

	// 소셜 로그인 버튼 클릭 시 API 호출
	@GetMapping("")
	@Operation(summary = "redirect url 초기 설정 API", description = "redirect url 반환 API입니다. 각 버튼에 url을 넣어주세요.")
	public ApiResponse<MemberResponseDto.SocialLoginLocationResponseDto> login() {
		String locationKakao =
			"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientIdKakao
				+ "&redirect_uri=" + redirectUriKakao;
		String locationGoogle =
			"https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + clientIdGoogle
				+ "&redirect_uri=" + redirectUriGoogle + "&scope=email profile";

		// 버튼에 해당 location 위치
		return ApiResponse.onSuccess(MemberConverter.toSocialLoginLocationResponseDto(locationKakao, locationGoogle));
	}

	@GetMapping("/code/kakao")
	@Operation(summary = "카카오 로그인 API", description = "카카오 로그인 API입니다.")
	@Parameters({
		@Parameter(name = "code", description = "카카오 API에 대한 response code, query parameter 입니다!")
	})
	public ApiResponse<?> kakaoLogin(@RequestParam("code") String code) {
		// 1. 외부 API 호출
		KakaoLoginDto.KakaoUserInfoResponseDto userInfo = kakaoLoginService.loginWithKakao(code);

		// 2. 회원 처리
		MemberResponseDto.SocialLoginResponseDto socialLoginResponseDto = memberCommandService.handleSocialLogin(
			userInfo.getKakaoAccount().getEmail(),
			LoginType.KAKAO
		);

		return ApiResponse.onSuccess(socialLoginResponseDto);
	}

	@GetMapping("/code/google")
	@Operation(summary = "구글 로그인 API", description = "구글 로그인 API입니다.")
	@Parameters({
		@Parameter(name = "code", description = "구글 API에 대한 response code, query parameter 입니다!")
	})
	public ApiResponse<?> googleLogin(@RequestParam("code") String code) {
		// 1. 외부 API 호출
		GoogleLoginDto.GoogleUserInfoResponseDto userInfo = googleLoginService.loginWithGoogle(code);

		// 2. 회원 처리
		MemberResponseDto.SocialLoginResponseDto socialLoginResponseDto = memberCommandService.handleSocialLogin(
			userInfo.getEmail(),
			LoginType.GOOGLE
		);

		return ApiResponse.onSuccess(socialLoginResponseDto);
	}

	@PostMapping("/kakao")
	@Operation(summary = "카카오 로그인 API", description = "카카오 로그인 API입니다.")
	public ApiResponse<?> kakaoLogin(@RequestBody MemberRequestDto.SocialLoginDto request) {
		// 회원 처리
		MemberResponseDto.SocialLoginResponseDto socialLoginResponseDto = memberCommandService.handleSocialLogin(
			request.getEmail(),
			LoginType.KAKAO
		);

		return ApiResponse.onSuccess(socialLoginResponseDto);
	}

	@PostMapping("/apple")
	@Operation(summary = "애플 로그인 API", description = "애플 로그인 API입니다.")
	public ApiResponse<?> appleLogin(@RequestBody MemberRequestDto.AccessTokenDto request) {
		// 1. 외부 API 호출
		AppleLoginDto.AppleUserInfoResponseDto userInfo = appleLoginService.loginWithApple(request.getAccessToken());

		// 2. 회원 처리
		MemberResponseDto.SocialLoginResponseDto socialLoginResponseDto = memberCommandService.handleSocialLogin(
			userInfo.getEmail(),
			LoginType.APPLE
		);

		return ApiResponse.onSuccess(socialLoginResponseDto);
	}
}
