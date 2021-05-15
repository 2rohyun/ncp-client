package com.ncp.ncpclient.sens.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRequestResponseDto {

    private String requestId;

    private String statusCode;

    private String statusName;

    private List<SearchRequestMessageResponseDto> messages;
}
