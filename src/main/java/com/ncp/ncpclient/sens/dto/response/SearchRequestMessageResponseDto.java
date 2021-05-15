package com.ncp.ncpclient.sens.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRequestMessageResponseDto {

    private String messageId;

    private String requestTime;

    private String contentType;

    private String countryCode;

    private String from;

    private String to;

}
