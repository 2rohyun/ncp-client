package com.ncp.ncpclient.sens.exception;

import com.ncp.ncpclient.common.exception.BaseExceptionType;

public enum SensExceptionType implements BaseExceptionType {

    INVALID_URL(1001, 200, "1001 Invalid URL"),
    UNEXPECTED_HTTP_STATUS_CODE(1002, 200, "1002 Unexpected HTTP Status Code : "),
    UNEXPECTED_RESPONSE(1003, 200, "1003 Unexpected response from the server"),
    NO_RESPONSE_FROM_SERVER(1004, 200, "1004 No response from the server"),
    REQUEST_CONFIGURATION_ERROR(1005, 200, "1005 Error occurred during setup request");

    private final int errorCode;

    private final int httpStatus;

    private final String errorMessage;

    SensExceptionType(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
