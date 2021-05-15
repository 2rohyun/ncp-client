package com.ncp.ncpclient.papago.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmtRequestDto {

    private String source;

    private String target;

    private String text;

    private boolean honorific;

    public NmtRequestDto(String source, String target, String text) {
        this.source = source;
        this.target = target;
        this.text = text;
    }
}
