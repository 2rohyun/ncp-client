package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.sens.api.InitService;
import com.ncp.ncpclient.sens.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

    private final String BASE_URL = "https://sens.apigw.ntruss.com";

    private final String BASE_URI = "/sms/v2/services/";

    private final String SEND_SMS_URI = "/messages";

    private final String SEARCH_MESSAGES_REQUEST_URI = "/messages?requestId=";

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content : message content
     * @return : ResponseEntity<SmsResponseDto>
     */
    public ResponseEntity<SmsResponseDto> sendSms(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content);
        HttpEntity<String> requestToJson = smsRequestToJson(smsRequestDto);

        // requested json format ( include header & body )
        log.info("request format : {}", requestToJson);

        ResponseEntity<SmsResponseDto> res = sendSmsRequest(requestToJson, BASE_URL + BASE_URI + serviceId + SEND_SMS_URI);

        // send post request, success = status code 202
        log.info("send sms response : {}",res);

        return res;
    }

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content :  message content
     * @param countryCode : country code
     * @return : ResponseEntity<SmsResponseDto>
     */
    public ResponseEntity<SmsResponseDto> sendSms(String recipientPhoneNumber, String content, String countryCode) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content, countryCode);
        HttpEntity<String> requestToJson = smsRequestToJson(smsRequestDto);

        // requested json format ( include header & body )
        log.info("request format : {}", requestToJson);

        ResponseEntity<SmsResponseDto> res = sendSmsRequest(requestToJson,BASE_URL + BASE_URI + serviceId + SEND_SMS_URI);

        // send post request, success = status code 202
        log.info("send sms response : {}",res);

        return res;
    }

    /**
     * @param requestId : sms request id
     * @return : ResponseEntity<SearchResponseDto>
     */
    public ResponseEntity<SearchRequestResponseDto> searchMessageRequest(String requestId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpEntity<String> requestToJson = searchMessageRequestToJson(requestId);
        ResponseEntity<SearchRequestResponseDto> res = sendSearchMessageRequest(requestToJson, BASE_URL + BASE_URI + serviceId + SEARCH_MESSAGES_REQUEST_URI + requestId);
        log.info("search message request's response : {}", res);
        return res;
    }

    /**
     * @param messageId : search message request's message id
     * @return : ResponseEntity<SearchResultResponseDto>
     */
    public ResponseEntity<SearchResultResponseDto> searchMessageResult(String messageId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpEntity<String> requestToJson = searchMessageResultToJson(messageId);
        ResponseEntity<SearchResultResponseDto> res = sendSearchMessageResult(requestToJson, BASE_URL + BASE_URI + serviceId + SEND_SMS_URI + "/" + messageId);
        log.info("search message result's response : {}", res);
        return res;
    }

    private ResponseEntity<SmsResponseDto> sendSmsRequest(HttpEntity<String> req, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.POST, req, SmsResponseDto.class);
    }

    private ResponseEntity<SearchResultResponseDto> sendSearchMessageResult(HttpEntity<String> requestToJson, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.GET, requestToJson, SearchResultResponseDto.class);
    }

    private ResponseEntity<SearchRequestResponseDto> sendSearchMessageRequest(HttpEntity<String> requestToJson, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.GET, requestToJson, SearchRequestResponseDto.class);
    }

    private HttpEntity<String> searchMessageResultToJson(String messageId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String time = Long.toString(System.currentTimeMillis());

        // header configuration
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", access);

        // signature
        String sig = initService.makeSignature(time,"GET", BASE_URI + serviceId + SEND_SMS_URI + "/" + messageId);
        headers.set("x-ncp-apigw-signature-v2", sig);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<String> searchMessageRequestToJson(String requestId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String time = Long.toString(System.currentTimeMillis());

        // header configuration
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", access);

        // signature
        String sig = initService.makeSignature(time,"GET", BASE_URI + serviceId + SEARCH_MESSAGES_REQUEST_URI + requestId);
        headers.set("x-ncp-apigw-signature-v2", sig);
        return new HttpEntity<>(headers);
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
        String sig = initService.makeSignature(time,"POST", BASE_URI + serviceId + SEND_SMS_URI);
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
