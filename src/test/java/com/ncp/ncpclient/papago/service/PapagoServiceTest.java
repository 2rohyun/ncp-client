package com.ncp.ncpclient.papago.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncp.ncpclient.papago.dto.response.NmtResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PapagoServiceTest {

    @Autowired
    private PapagoService papagoService;

    @Test
    @DisplayName("번역 성공 : 한국어 -> 영어")
    void translationKoToEn() throws JsonProcessingException, URISyntaxException {
        ResponseEntity<NmtResponseDto> translation = papagoService.translation("ko", "en", "안녕하세요 저는 이도현입니다.");
        System.out.println("translation = " + translation);

    }

    @Test
    @DisplayName("번역 성공 : 영어 -> 한국어 높임말")
    void translationEnToKoHonorific() throws JsonProcessingException, URISyntaxException {
        ResponseEntity<NmtResponseDto> translation = papagoService.translation("en", "ko", "hello, I am dohyun! nice to meet you", true);
        System.out.println("translation = " + translation);

    }

}