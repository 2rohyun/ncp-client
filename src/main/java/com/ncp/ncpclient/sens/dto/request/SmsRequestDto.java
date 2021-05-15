package com.ncp.ncpclient.sens.dto.request;


import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {

    @NotBlank
    private String type;

    private String contentType;

    private String countryCode;

    @NotBlank
    private String from;

    @NotBlank
    private String content;

    @NotBlank
    private List<MessagesRequestDto> messages;

}
