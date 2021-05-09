package com.ncp.ncpclient.sens.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagesRequestDto {

    private String to;

    private String content;

}
