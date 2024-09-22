package org.example.bodycheck.domain.social_login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;
import org.example.bodycheck.domain.social_login.SocialLoginDTO.GoogleDTO;
import org.example.bodycheck.domain.social_login.SocialLoginDTO.KakaoDTO;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.example.bodycheck.domain.social_login.SocialLoginService.GoogleService;
import org.example.bodycheck.domain.social_login.SocialLoginService.KakaoService;
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
public class SocialLoginRestController {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
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
    public ApiResponse<MemberResponseDTO.SocialLoginLocationResponseDTO> login() {
        String locationKakao = "https://kakao.com/oauth2/authorize?response_type=code&client_id=" + client_id_kakao + "&redirect_uri=" + redirect_uri_kakao;
        String locationGoogle = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + client_id_google + "&redirect_uri=" + redirect_uri_google + "&scope=email profile";

        // 버튼에 해당 location 위치
        return ApiResponse.onSuccess(MemberConverter.toSocialLoginLocationResponseDTO(locationKakao, locationGoogle));
    }

    @GetMapping("/code/kakao")
    public ApiResponse<MemberResponseDTO.SocialLoginResponseDTO> kakaoLogin(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoDTO.KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);

        String nickname = userInfo.getKakaoAccount().getProfile().getNickName();
        String email = userInfo.getKakaoAccount().getEmail();

        boolean isUser = memberCommandService.isUser(email);

        JwtTokenDTO jwtTokenDTO;

        if (isUser) {
            jwtTokenDTO = memberCommandService.socialLogin(email);
        }
        else {
            jwtTokenDTO = null;
        }

        return ApiResponse.onSuccess(MemberConverter.toSocialLoginResponseDTO(isUser, email, nickname, jwtTokenDTO));
    }

    @GetMapping("/code/google")
    public ApiResponse<MemberResponseDTO.SocialLoginResponseDTO> googleLogin(@RequestParam("code") String code) {
        String accessToken = googleService.getAccessTokenFromGoogle(code);

        GoogleDTO.GoogleUserInfoResponseDTO userInfo = googleService.getUserInfo(accessToken);

        String nickname = userInfo.getNickName();
        String email = userInfo.getEmail();

        boolean isUser = memberCommandService.isUser(email);

        JwtTokenDTO jwtTokenDTO;

        if (isUser) {
            jwtTokenDTO = memberCommandService.socialLogin(email);
        }
        else {
            jwtTokenDTO = null;
        }

        return ApiResponse.onSuccess(MemberConverter.toSocialLoginResponseDTO(isUser, email, nickname, jwtTokenDTO));
    }
}
