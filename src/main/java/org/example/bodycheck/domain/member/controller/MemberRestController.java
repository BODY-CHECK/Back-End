package org.example.bodycheck.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.bodycheck.common.jwt.JwtTokenDTO;
import org.example.bodycheck.common.apiPayload.ApiResponse;
import org.example.bodycheck.domain.member.converter.MemberConverter;
import org.example.bodycheck.domain.member.entity.Member;
import org.example.bodycheck.domain.member.service.MemberService.MemberCommandService;
import org.example.bodycheck.domain.member.service.MemberService.MemberQueryService;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberRequestDTO;
import org.example.bodycheck.domain.member.dto.MemberDTO.MemberResponseDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/email/sign-up")
    public ApiResponse<MemberResponseDTO.SignUpResponseDTO> signUp(@RequestBody MemberRequestDTO.SignUpDTO request) {
        Member member = memberCommandService.signUp(request);
        return ApiResponse.onSuccess(MemberConverter.toSignUpResponseDTO(member));
    }

    @PostMapping("/email/sign-in")  // JWT 토큰을 생성하여 반환
    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> signIn(@RequestBody MemberRequestDTO.SignInDTO request) {
        JwtTokenDTO jwtTokenDTO = memberCommandService.signIn(request);
        return ApiResponse.onSuccess(MemberConverter.accessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberQueryService.getMember();
        Long memberId = member.getId();

        memberCommandService.logout(memberId);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/refresh-token")
    public ApiResponse<MemberResponseDTO.AccessTokenResponseDTO> refreshToken(@RequestBody MemberRequestDTO.refreshTokenDTO request) {
        JwtTokenDTO jwtTokenDTO = memberCommandService.refreshToken(request);
        return ApiResponse.onSuccess(MemberConverter.accessTokenResponseDTO(jwtTokenDTO));
    }

    @PostMapping("/verify-password")
    public ApiResponse<String> verifyPassword(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody MemberRequestDTO.PasswordDTO request) {
        //System.out.println(authorizationHeader + " authorizationHeader");

        Member member = memberQueryService.getMember();
        //System.out.println(member + " member");
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
    public ApiResponse<String> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody MemberRequestDTO.PasswordDTO request) {
        Member member = memberQueryService.getMember();
        Long memberId = member.getId();

        String comment = memberCommandService.changePassword(memberId, request);
        return ApiResponse.onSuccess(comment);
    }
}
