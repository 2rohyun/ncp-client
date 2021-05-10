package com.ncp.ncpclient.sens.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultResponseDto {

    private String statusCode;

    private String statusName;

    private List<SearchResultMessageResponseDto> messages;

}

