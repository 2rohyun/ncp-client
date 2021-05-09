package com.ncp.ncpclient.sens.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SmsResponseDto {

    private String statusCode;

    private String statusName;

    private String requestId;

    private LocalDateTime requestTime;

}
