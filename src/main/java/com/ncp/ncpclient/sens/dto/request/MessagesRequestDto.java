package com.ncp.ncpclient.sens.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagesRequestDto {

    private String to;

    private String content;

}
