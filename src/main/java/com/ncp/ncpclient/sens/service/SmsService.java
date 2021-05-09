package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.sens.api.InitService;
import com.ncp.ncpclient.sens.dto.MessagesRequestDto;
import com.ncp.ncpclient.sens.dto.SmsRequestDto;
import com.ncp.ncpclient.sens.dto.SmsResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:ncp.properties")
@Slf4j
@Getter
public class SmsService {

    private final InitService initService;

    @Value("${api.accessKey}")
    private String access;

    @Value("${sms.sendFrom}")
    private String sendFrom;

    @Value("${sms.serviceId}")
    private String serviceId;

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content : message content
     * @return : smsResponseDto
     */
    public SmsResponseDto sendSms(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content);
        HttpEntity<String> body = smsRequestToJson(smsRequestDto);

        // requested json format ( include header & body )
        log.info("request format : {}", body);

        SmsResponseDto smsResponseDto = sendRequest(body);

        // send post request, success = status code 202
        log.info("status code : {}",smsResponseDto.getStatusCode());

        return smsResponseDto;
    }

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content :  message content
     * @param countryCode : country code
     * @return : smsResponseDto
     */
    public SmsResponseDto sendSms(String recipientPhoneNumber, String content, String countryCode) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content, countryCode);
        HttpEntity<String> body = smsRequestToJson(smsRequestDto);

        // requested json format ( include header & body )
        log.info("request format : {}", body);

        SmsResponseDto smsResponseDto = sendRequest(body);

        // send post request, success = status code 202
        log.info("status code : {}",smsResponseDto.getStatusCode());

        return smsResponseDto;
    }

    private SmsResponseDto sendRequest(HttpEntity<String> body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"), body, SmsResponseDto.class);
    }

    private HttpEntity<String> smsRequestToJson(SmsRequestDto smsRequestDto) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // body to json
        String time = Long.toString(System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequestDto);

        // header configuration
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", access);

        // signature
        String sig = initService.makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        // json body + json header
        return new HttpEntity<>(jsonBody, headers);
    }

    private SmsRequestDto createSmsRequest(String recipientPhoneNumber, String content) {
        List<MessagesRequestDto> messages = new ArrayList<>();

        // add message content
        messages.add(new MessagesRequestDto(recipientPhoneNumber, content));

        // create message to json type and return
        return new SmsRequestDto("SMS", "COMM", "82", sendFrom," ", messages);
    }

    private SmsRequestDto createSmsRequest(String recipientPhoneNumber, String content, String countryCode) {
        List<MessagesRequestDto> messages = new ArrayList<>();

        // add message content
        messages.add(new MessagesRequestDto(recipientPhoneNumber, content));

        // create message to json type and return
        return new SmsRequestDto("SMS", "COMM", countryCode, sendFrom," ", messages);
    }
}
