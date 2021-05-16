package com.ncp.ncpclient.papago.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NmtMessageResponseDto {

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@service")
    private String service;

    @JsonProperty("@version")
    private String version;

    private NmtMessageResultResponseDto result;

}
