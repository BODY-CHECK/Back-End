package org.example.bodycheck.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bodycheck.apiPayload.code.BaseErrorCode;
import org.example.bodycheck.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
