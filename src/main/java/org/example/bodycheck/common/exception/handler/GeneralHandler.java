package org.example.bodycheck.common.exception.handler;

import org.example.bodycheck.common.apiPayload.code.BaseErrorCode;
import org.example.bodycheck.common.exception.GeneralException;

public class GeneralHandler extends GeneralException {

    public GeneralHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
