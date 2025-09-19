package org.example.bodycheck.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenDto;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.member.dto.memberdto.MemberRequestDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberResponseDto;
import org.example.bodycheck.external.kakao.pay.service.KakaoPayService;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.memberdto.MemberProfileSettingDto;
import org.example.bodycheck.domain.member.dto.memberdto.MemberSettingDto;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.memberservice.MemberCommandService;
import org.example.bodycheck.domain.member.service.memberservice.SettingService;
import org.example.bodycheck.domain.routine.service.RoutineService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final SettingService settingService;
    private final RoutineService routineService;
    private final KakaoPayService kakaoPayService;

    @PostMapping("/email/sign-up")
    @Operation(summary = "회원가입 API", description = "이메일로 회원가입을 하는 API 입니다.")
    public ApiResponse<MemberResponseDto.AccessTokenResponseDto> signUp(@Valid @RequestBody MemberRequestDto.SignUpDto request) {
        Member member = memberCommandService.signUp(request);
        routineService.initRoutine(member);
        JwtTokenDto jwtTokenDTO = memberCommandService.directLogin(member);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/deactivate")
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴하는 API 입니다.")
    public ApiResponse<String> deactivate(@AuthUser Member member,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        memberCommandService.deactivate(member, token);
        return ApiResponse.onSuccess("OK");
    }

    @PostMapping("/email/sign-in")  // JWT 토큰을 생성하여 반환
    @Operation(summary = "로그인 API", description = "로그인을 하는 API 입니다.")
    public ApiResponse<MemberResponseDto.AccessTokenResponseDto> signIn(@RequestBody MemberRequestDto.SignInDto request) {
        JwtTokenDto jwtTokenDTO = memberCommandService.signIn(request);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

//    @PostMapping("/social-login")  // JWT 토큰을 생성하여 반환
//    @Operation(summary = "소셜 로그인 API", description = "소셜로 로그인을 하는 API 입니다.")
//    public ApiResponse<MemberResponseDto.AccessTokenResponseDto> socialLogin(@RequestBody MemberRequestDto.SocialLoginDto request) {
//        JwtTokenDto jwtTokenDTO = memberCommandService.socialLogin(request.getEmail());
//        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
//    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃을 하는 API 입니다.")
    public ApiResponse<String> logout(@AuthUser Member member,
                                      @RequestHeader("Authorization") String authorizationHeader) {
        String email = member.getEmail();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        memberCommandService.logout(email, token);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "리프레시 토큰으로 인가를 확인하는 API", description = "리프레시 토큰으로 인가를 확인하는 API 입니다.")
    public ApiResponse<MemberResponseDto.AccessTokenResponseDto> refreshToken(@RequestBody MemberRequestDto.refreshTokenDto request) {
        JwtTokenDto jwtTokenDTO = memberCommandService.refreshToken(request);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/verify-password")
    @Operation(summary = "비밀번호 검증 API", description = "비밀번호를 이용하여 사용자 본인이 맞는지 확인하는 API 입니다.")
    public ApiResponse<String> verifyPassword(@AuthUser Member member,
                                              @RequestBody MemberRequestDto.PasswordDto request) {
        Long memberId = member.getId();
        boolean isChecked = memberCommandService.verifyPassword(memberId, request);
        if (isChecked) {
            return ApiResponse.onSuccess("비밀번호 인증이 완료되었습니다.");
        }
        else {
            return ApiResponse.onFailure("400", "비밀번호 인증에 실패했습니다.", "비밀번호가 틀렸습니다.");
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경 API", description = "비밀번호를 변경하는 API 입니다.")
    public ApiResponse<String> changePassword(@AuthUser Member member,
                                              @RequestBody MemberRequestDto.PasswordDto request) {
        Long memberId = member.getId();

        String comment = memberCommandService.changePassword(memberId, request);
        return ApiResponse.onSuccess(comment);
    }

    @GetMapping("/my-page")  // JWT 토큰을 생성하여 반환
    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 정보를 조회하는 API 입니다.")
    public ApiResponse<MemberResponseDto.MyPageResponseDto> myPage(@AuthUser Member member) {

        Long memberId = member.getId();

        boolean isPremium = kakaoPayService.getPremiumState(memberId);

        return ApiResponse.onSuccess(MemberConverter.toMyPageResponseDTO(member, isPremium));
    }

    @CrossOrigin
    @PostMapping("/setting/profile")
    @Operation(summary = "프로필 변경 API")
    public ApiResponse<MemberSettingDto> profileSetting(@AuthUser Member member, @RequestBody MemberProfileSettingDto request) {
        return ApiResponse.onSuccess(settingService.profileSetting(member, request));
    }
}
