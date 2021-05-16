package com.ncp.ncpclient.papago.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KoreanNameRomanizerResponseDto {

    private List<AResultResponseDto> aResult;

    public List<AResultResponseDto> getaResult() {
        return aResult;
    }

    public void setaResult(List<AResultResponseDto> aResult) {
        this.aResult = aResult;
    }
}
