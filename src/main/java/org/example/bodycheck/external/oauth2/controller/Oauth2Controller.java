package org.example.bodycheck.external.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;
import org.example.bodycheck.external.oauth2.google.dto.GoogleLoginDto;
import org.example.bodycheck.external.oauth2.kakao.dto.KakaoLoginDto;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.example.bodycheck.external.oauth2.google.service.GoogleLoginService;
import org.example.bodycheck.external.oauth2.kakao.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/login/oauth2")
public class Oauth2Controller {

    private final KakaoLoginService kakaoLoginService;
    private final GoogleLoginService googleLoginService;
    private final MemberCommandService memberCommandService;

    @Value("${spring.kakao.client_id}")
    private String client_id_kakao;

    @Value("${spring.kakao.redirect_uri}")
    private String redirect_uri_kakao;

    @Value("${spring.google.client_id}")
    private String client_id_google;

    @Value("${spring.google.redirect_uri}")
    private String redirect_uri_google;

    // 소셜 로그인 버튼 클릭 시 API 호출
    @GetMapping("")
    @Operation(summary = "redirect url 초기 설정 API", description = "redirect url 반환 API입니다. 각 버튼에 url을 넣어주세요.")
    public ApiResponse<MemberResponseDTO.SocialLoginLocationResponseDTO> login() {
        String locationKakao = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id_kakao + "&redirect_uri=" + redirect_uri_kakao;
        String locationGoogle = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + client_id_google + "&redirect_uri=" + redirect_uri_google + "&scope=email profile";

        // 버튼에 해당 location 위치
        return ApiResponse.onSuccess(MemberConverter.toSocialLoginLocationResponseDTO(locationKakao, locationGoogle));
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
        MemberResponseDTO.SocialLoginResponseDTO socialLoginResponseDTO = memberCommandService.handleSocialLogin(
                userInfo.getKakaoAccount().getEmail(),
                userInfo.getKakaoAccount().getProfile().getNickName()
                );

        return ApiResponse.onSuccess(socialLoginResponseDTO);
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
        MemberResponseDTO.SocialLoginResponseDTO socialLoginResponseDTO = memberCommandService.handleSocialLogin(
                userInfo.getEmail(),
                userInfo.getNickName()
                );

        return ApiResponse.onSuccess(socialLoginResponseDTO);
    }
}
