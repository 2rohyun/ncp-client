package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class SmsServiceTest {

    @Autowired
    private SmsService smsService;


    @Test
    @DisplayName("문자 메세지 전송 - 성공 ( 국가 코드 default )")
    void sendSmsWithDefaultCountryCode() throws NoSuchAlgorithmException, URISyntaxException, UnsupportedEncodingException, InvalidKeyException, JsonProcessingException {
        smsService.sendSms("01072117883","문자 제대로 가는지 테스트임ㅋ");

    }
    @Test
    @DisplayName("문자 메세지 전송 - 성공 ( 국가 코드 입력 )")
    void sendSmsWithInputCountryCode() throws NoSuchAlgorithmException, URISyntaxException,InvalidKeyException, JsonProcessingException {
        smsService.sendSms("01072117883","문자 제대로 가는지 테스트임ㅋ", "82");

    }

    @Test
    @DisplayName("요청 매세지 조회 - 성공")
    void searchMessageRequestTest() throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        smsService.searchMessageRequest("7d6841f9380a403fb366f6f54dc66b03");
    }

    @Test
    @DisplayName("응답 매세지 조회 - 성공")
    void searchMessageResultTest() throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        smsService.searchMessageResult("0-ESA-202105-4473189-0");
    }

}