package com.ncp.ncpclient.sens.exception;

import com.ncp.ncpclient.common.exception.BaseExceptionType;
import lombok.Getter;

public class SensException extends RuntimeException {

    @Getter
    private final BaseExceptionType exceptionType;

    public SensException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
}
