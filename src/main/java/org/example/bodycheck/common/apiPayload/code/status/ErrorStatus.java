package org.example.bodycheck.common.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bodycheck.common.apiPayload.code.BaseErrorCode;
import org.example.bodycheck.common.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 로그인 관련
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "email already exists", "이미 존재하는 이메일입니다."),
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "LOGIN FAIL", "아이디 또는 비밀번호를 확인하세요"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password mismatch", "비밀번호가 일치하지 않습니다."),
    VERIFICATION_CODE_NOT_EXIST(HttpStatus.BAD_REQUEST, "code not exist", "인증코드가 존재하지 않습니다."),

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // Token Error
    TOKEN_MISSING_AUTHORITY(HttpStatus.UNAUTHORIZED, "TOKEN401", "권한 정보가 없는 토큰입니다."),
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN402", "유효하지 않은 토큰입니다."),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN403", "잘못된 JWT 서명입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN404", "만료된 JWT 토큰입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "TOKEN405", "지원되지 않는 JWT 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
