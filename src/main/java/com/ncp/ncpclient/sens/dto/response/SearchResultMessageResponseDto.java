package com.ncp.ncpclient.sens.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultMessageResponseDto {

    private String requestTime;

    private String contentType;

    private String content;

    private String countryCode;

    private String from;

    private String to;

    private String status;

    private String statusCode;

    private String statusMessage;

    private String statusName;

    private String completeTime;

    private String telcoCode;

}
