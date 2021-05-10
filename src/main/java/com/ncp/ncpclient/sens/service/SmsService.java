package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.sens.api.InitService;
import com.ncp.ncpclient.sens.dto.request.MessagesRequestDto;
import com.ncp.ncpclient.sens.dto.request.SmsRequestDto;
import com.ncp.ncpclient.sens.dto.response.SearchRequestResponseDto;
import com.ncp.ncpclient.sens.dto.response.SearchResultResponseDto;
import com.ncp.ncpclient.sens.dto.response.SmsResponseDto;
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

    private final String BASE_SMS_URL = "/sms/v2/services/";

    private final String SEND_SMS_URL = "/messages";

    private final String SEARCH_MESSAGES_REQUEST_URL = "/messages?requestId=";

    private final String SEARCH_MESSAGES_RESULT_URL = "/messages/";

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content : message content
     * @return : ResponseEntity<SmsResponseDto>
     */
    public ResponseEntity<SmsResponseDto> sendSms(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {

        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content);
        HttpEntity<String> requestToJson = smsRequestToJson(smsRequestDto);

        // requested json format ( include header & body )
        log.info("requested json : {}", requestToJson);

        ResponseEntity<SmsResponseDto> res = sendSmsRequest(requestToJson, BASE_URL + BASE_SMS_URL + serviceId + SEND_SMS_URL);

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
        log.info("requested json : {}", requestToJson);

        ResponseEntity<SmsResponseDto> res = sendSmsRequest(requestToJson,BASE_URL + BASE_SMS_URL + serviceId + SEND_SMS_URL);

        // send post request, success = status code 202
        log.info("send sms response : {}",res);

        return res;
    }

    /**
     * @param requestId : sms request id
     * @return : ResponseEntity<SearchRequestResponseDto>
     */
    public ResponseEntity<SearchRequestResponseDto> searchMessageRequest(String requestId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpEntity<String> requestToJson = searchMessageRequestToJson(requestId);
        ResponseEntity<SearchRequestResponseDto> res = sendSearchMessageRequest(requestToJson, BASE_URL + BASE_SMS_URL + serviceId + SEARCH_MESSAGES_REQUEST_URL + requestId);
        log.info("search message request's response : {}", res);
        return res;
    }

    /**
     * @param messageId : search message request's message id
     * @return : ResponseEntity<SearchResultResponseDto>
     */
    public ResponseEntity<SearchResultResponseDto> searchMessageResult(String messageId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpEntity<String> requestToJson = searchMessageResultToJson(messageId);
        ResponseEntity<SearchResultResponseDto> res = sendSearchMessageResult(requestToJson, BASE_URL + BASE_SMS_URL + serviceId + SEARCH_MESSAGES_RESULT_URL + messageId);
        log.info("search message result's response : {}", res);
        return res;
    }

    private ResponseEntity<SmsResponseDto> sendSmsRequest(HttpEntity<String> req, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.POST, req, SmsResponseDto.class);
    }

    private ResponseEntity<SearchRequestResponseDto> sendSearchMessageRequest(HttpEntity<String> requestToJson, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.GET, requestToJson, SearchRequestResponseDto.class);
    }

    private ResponseEntity<SearchResultResponseDto> sendSearchMessageResult(HttpEntity<String> requestToJson, String url) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), HttpMethod.GET, requestToJson, SearchResultResponseDto.class);
    }

    private HttpEntity<String> smsRequestToJson(SmsRequestDto smsRequestDto) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // body to json format
        String jsonBody = getHttpBody(smsRequestDto);

        // header configuration
        HttpHeaders headers = getHttpHeader(Long.toString(System.currentTimeMillis()));

        // signature
        String sig = initService.makeSignature(Long.toString(System.currentTimeMillis()),"POST", BASE_SMS_URL + serviceId + SEND_SMS_URL);
        headers.set("x-ncp-apigw-signature-v2", sig);

        // json body + json header
        return new HttpEntity<>(jsonBody, headers);
    }

    private HttpEntity<String> searchMessageResultToJson(String messageId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // header configuration
        HttpHeaders headers = getHttpHeader(Long.toString(System.currentTimeMillis()));

        // signature
        String sig = initService.makeSignature(Long.toString(System.currentTimeMillis()),"GET", BASE_SMS_URL + serviceId + SEARCH_MESSAGES_RESULT_URL + messageId);
        headers.set("x-ncp-apigw-signature-v2", sig);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<String> searchMessageRequestToJson(String requestId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // header configuration
        HttpHeaders headers = getHttpHeader(Long.toString(System.currentTimeMillis()));

        // signature
        String sig = initService.makeSignature(Long.toString(System.currentTimeMillis()),"GET", BASE_SMS_URL + serviceId + SEARCH_MESSAGES_REQUEST_URL + requestId);
        headers.set("x-ncp-apigw-signature-v2", sig);
        return new HttpEntity<>(headers);
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

    private HttpHeaders getHttpHeader(String time) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", access);
        return headers;
    }

    private String getHttpBody(SmsRequestDto smsRequestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(smsRequestDto);
    }
}
