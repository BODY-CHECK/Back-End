package org.example.bodycheck.apiPayload.exception.handler;

import org.example.bodycheck.apiPayload.code.BaseErrorCode;
import org.example.bodycheck.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
