package com.ncp.ncpclient.papago.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NmtMessageResponseDto {

    private String type;

    private String service;

    private String version;

    private NmtMessageResultResponseDto result;

}
