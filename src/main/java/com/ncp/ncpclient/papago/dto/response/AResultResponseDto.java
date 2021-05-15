package com.ncp.ncpclient.papago.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AResultResponseDto {

    private String sFirstName;

    private List<AItemsResponseDto> aItems;

}
