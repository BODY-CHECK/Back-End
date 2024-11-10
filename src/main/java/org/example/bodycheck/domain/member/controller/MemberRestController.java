package org.example.bodycheck.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.member.annotation.AuthUser;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberProfileSettingDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberSettingDTO;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.example.bodycheck.domain.member.service.MemberService.MemberQueryService;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;
import org.example.bodycheck.domain.member.service.MemberService.SettingService;
import org.example.bodycheck.domain.routine.service.RoutineService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final SettingService settingService;
    private final RoutineService routineService;

    @PostMapping("/email/sign-up")
    @Operation(summary = "회원가입 API", description = "이메일로 회원가입을 하는 API 입니다.")
    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> signUp(@Valid @RequestBody MemberRequestDTO.SignUpDTO request) {
        Member member = memberCommandService.signUp(request);
        routineService.setRoutine(member);
        JwtTokenDTO jwtTokenDTO = memberCommandService.directLogin(member);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/email/sign-in")  // JWT 토큰을 생성하여 반환
    @Operation(summary = "로그인 API", description = "로그인을 하는 API 입니다.")
    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> signIn(@RequestBody MemberRequestDTO.SignInDTO request) {
        JwtTokenDTO jwtTokenDTO = memberCommandService.signIn(request);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

//    @PostMapping("/social-login")  // JWT 토큰을 생성하여 반환
//    @Operation(summary = "소셜 로그인 API", description = "소셜로 로그인을 하는 API 입니다.")
//    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> socialLogin(@RequestBody MemberRequestDTO.SocialLoginDTO request) {
//        JwtTokenDTO jwtTokenDTO = memberCommandService.socialLogin(request.getEmail());
//        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
//    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃을 하는 API 입니다.")
    public ApiResponse<String> logout(@AuthUser Member member) {
        Long memberId = member.getId();

        memberCommandService.logout(memberId);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "리프레시 토큰으로 인가를 확인하는 API", description = "리프레시 토큰으로 인가를 확인하는 API 입니다.")
    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> refreshToken(@RequestBody MemberRequestDTO.refreshTokenDTO request) {
        JwtTokenDTO jwtTokenDTO = memberCommandService.refreshToken(request);
        return ApiResponse.onSuccess(MemberConverter.toAccessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/verify-password")
    @Operation(summary = "비밀번호 검증 API", description = "비밀번호를 이용하여 사용자 본인이 맞는지 확인하는 API 입니다.")
    public ApiResponse<String> verifyPassword(@AuthUser Member member,
                                              @RequestBody MemberRequestDTO.PasswordDTO request) {
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
                                              @RequestBody MemberRequestDTO.PasswordDTO request) {
        Long memberId = member.getId();

        String comment = memberCommandService.changePassword(memberId, request);
        return ApiResponse.onSuccess(comment);
    }

    @GetMapping("/my-page")  // JWT 토큰을 생성하여 반환
    @Operation(summary = "마이페이지 조회 API", description = "마이페이지 정보를 조회하는 API 입니다.")
    public ApiResponse<MemberResponseDTO.MyPageResponseDTO> myPage(@AuthUser Member member) {

        return ApiResponse.onSuccess(MemberConverter.toMyPageResponseDTO(member));
    }

    @CrossOrigin
    @PostMapping("/setting/profile")
    @Operation(summary = "프로필 변경 API")
    public ApiResponse<MemberSettingDTO> profileSetting(@AuthUser Member member, MemberProfileSettingDTO request) {
        return ApiResponse.onSuccess(settingService.profileSetting(member, request));
    }
}
