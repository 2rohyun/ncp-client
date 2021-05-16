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

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public List<AItemsResponseDto> getaItems() {
        return aItems;
    }

    public void setaItems(List<AItemsResponseDto> aItems) {
        this.aItems = aItems;
    }
}
