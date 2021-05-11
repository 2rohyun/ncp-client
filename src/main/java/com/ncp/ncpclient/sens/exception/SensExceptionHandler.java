package com.ncp.ncpclient.sens.exception;

import com.ncp.ncpclient.common.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SensExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(SensException.class)
    public ResponseEntity<Error> exception(SensException e) {
        return new ResponseEntity<>(Error.create(e.getExceptionType()), HttpStatus.OK);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Error {
        private int code;
        private int status;
        private String message;

        static Error create(BaseExceptionType e) {
            return new Error(e.getErrorCode(), e.getHttpStatus(), e.getErrorMessage());
        }
    }
}
