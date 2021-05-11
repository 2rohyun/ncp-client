package com.ncp.ncpclient.common.exception;

public interface BaseExceptionType {

    int getErrorCode();

    int getHttpStatus();

    String getErrorMessage();

}
