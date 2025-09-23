package org.example.bodycheck.common.exception;

import org.example.bodycheck.common.apipayload.code.BaseErrorCode;
import org.example.bodycheck.common.apipayload.code.ErrorReasonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

	private BaseErrorCode code;

	public ErrorReasonDto getErrorReason() {
		return this.code.getReason();
	}

	public ErrorReasonDto getErrorReasonHttpStatus() {
		return this.code.getReasonHttpStatus();
	}
}
