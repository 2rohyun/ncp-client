package com.ncp.ncpclient.sens.dto;


import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {

    private String type;

    private String contentType;

    private String countryCode;

    private String from;

    private String content;

    private List<MessagesRequestDto> messages;

}
