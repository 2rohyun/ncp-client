package com.ncp.ncpclient.papago.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmtRequestDto {

    @NotBlank
    private String source;

    @NotBlank
    private String target;

    @NotBlank
    private String text;

    private boolean honorific;

    public NmtRequestDto(String source, String target, String text) {
        this.source = source;
        this.target = target;
        this.text = text;
    }
}
