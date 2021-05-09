package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncp.ncpclient.sens.api.InitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmsServiceTest {

    @Autowired
    private SmsService smsService;

    @Autowired
    private InitService initService;

    @Test
    @DisplayName("문자 메세지 전송 - 성공")
    void sendSmsTest() throws NoSuchAlgorithmException, URISyntaxException, UnsupportedEncodingException, InvalidKeyException, JsonProcessingException {
        smsService.sendSms("01072117883","문자 제대로 가는지 테스트임ㅋ");

    }

    @Test
    void generateSig() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        initService.makeSignature(Long.toString(System.currentTimeMillis()));
    }
}