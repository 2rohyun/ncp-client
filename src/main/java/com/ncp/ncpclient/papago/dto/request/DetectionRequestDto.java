package com.ncp.ncpclient.papago.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectionRequestDto {

    @NotBlank
    private String query;

}
