package com.ncp.ncpclient.sens.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestMessageResponseDto {

    private String messageId;

    private String requestTime;

    private String contentType;

    private String countryCode;

    private String from;

    private String to;

}
