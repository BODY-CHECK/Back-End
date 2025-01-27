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
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "email not found", "이메일이 존재하지 않습니다."),
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "LOGIN FAIL", "아이디 또는 비밀번호를 확인하세요"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password mismatch", "비밀번호가 일치하지 않습니다."),
    VERIFICATION_CODE_NOT_EXIST(HttpStatus.BAD_REQUEST, "code not exist", "인증코드가 존재하지 않습니다."),

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4003", "이미 존재하는 닉네임입니다."),
    TID_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4004", "tid가 존재하지 않습니다."),
    TID_SID_UNSUPPORTED(HttpStatus.BAD_REQUEST, "MEMBER4005", "지원되지 않는 tid, sid 입니다."),
    SID_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4006", "sid가 존재하지 않습니다."),
    KAKAO_USER(HttpStatus.BAD_REQUEST, "MEMBER4007", "카카오 유저에게는 지원하지 않는 기능입니다."),

    // Token Error
    TOKEN_MISSING_AUTHORITY(HttpStatus.UNAUTHORIZED, "TOKEN4001", "권한 정보가 없는 토큰입니다."),
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN4002", "유효하지 않은 토큰입니다."),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN4003", "잘못된 JWT 서명입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN4004", "만료된 JWT 토큰입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "TOKEN4005", "지원되지 않는 JWT 토큰입니다."),

    // Exercise Error
    EXERCISE_NOT_FOUND(HttpStatus.BAD_REQUEST, "EXERCISE4001", "운동이 없습니다."),
    EXERCISE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "EXERCISE4002", "유효하지 않은 운동 부위입니다."),

    // Solution Error
    SOLUTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "SOLUTION4001", "솔루션이 없습니다."),
    SOLUTION_VIDEO_NOT_FOUND(HttpStatus.BAD_REQUEST, "SOLUTION4002", "솔루션 비디오가 없습니다."),
    SOLUTION_PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "SOLUTION4003", "솔루션 목록을 조회할 수 없습니다. 유효하지 않은 페이지입니다."),
    SOLUTION_CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "SOLUTION4004", "지원하지 않는 달입니다."),

    // Routine Error
    ROUTINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROUTINE4001", "해당 루틴은 존재하지 않습니다."),
    ROUTINE_WEEK_NOT_FOUND(HttpStatus.BAD_REQUEST, "ROUTINE4002", "해당 요일의 루틴은 존재하지 않습니다."),
    INVALID_ROUTINE_CHECK(HttpStatus.BAD_REQUEST, "ROUTINE4003", "해당 운동의 요일이 맞지 않습니다."),

    // Attendance Error
    ATTENDANCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ATTENDANCE4001", "해당 출석 체크가 없습니다");


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
