package com.ncp.ncpclient.sens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.sens.api.InitService;
import com.ncp.ncpclient.sens.dto.request.MessagesRequestDto;
import com.ncp.ncpclient.sens.dto.request.SmsRequestDto;
import com.ncp.ncpclient.sens.dto.response.SearchRequestResponseDto;
import com.ncp.ncpclient.sens.dto.response.SearchResultResponseDto;
import com.ncp.ncpclient.sens.dto.response.SmsResponseDto;
import com.ncp.ncpclient.sens.utils.ApiRequestUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.ncp.ncpclient.sens.utils.ApiRequestUtil.*;

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
        return sendRequest(smsRequestToJson(smsRequestDto), BASE_URL + BASE_SMS_URL + serviceId + SEND_SMS_URL, HttpMethod.POST);
    }

    /**
     * @param recipientPhoneNumber : recipient's phone number
     * @param content :  message content
     * @param countryCode : country code
     * @return : ResponseEntity<SmsResponseDto>
     */
    public ResponseEntity<SmsResponseDto> sendSms(String recipientPhoneNumber, String content, String countryCode) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        SmsRequestDto smsRequestDto = createSmsRequest(recipientPhoneNumber, content, countryCode);
        return sendRequest(smsRequestToJson(smsRequestDto),BASE_URL + BASE_SMS_URL + serviceId + SEND_SMS_URL, HttpMethod.POST);
    }

    /**
     * @param requestId : sms request id
     * @return : ResponseEntity<SearchRequestResponseDto>
     */
    public ResponseEntity<SearchRequestResponseDto> searchMessageRequest(String requestId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        return sendRequest(searchMessageRequestToJson(requestId),
                        BASE_URL + BASE_SMS_URL + serviceId + SEARCH_MESSAGES_REQUEST_URL + requestId,
                            HttpMethod.GET);
    }

    /**
     * @param messageId : search message request's message id
     * @return : ResponseEntity<SearchResultResponseDto>
     */
    public ResponseEntity<SearchResultResponseDto> searchMessageResult(String messageId) throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        return sendRequest(searchMessageResultToJson(messageId),
                        BASE_URL + BASE_SMS_URL + serviceId + SEARCH_MESSAGES_RESULT_URL + messageId,
                            HttpMethod.GET);
    }

    private HttpEntity<String> smsRequestToJson(SmsRequestDto smsRequestDto) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // body to json format
        String jsonBody = getHttpBody(smsRequestDto);

        // header configuration
        String time = Long.toString(System.currentTimeMillis());
        HttpHeaders headers = getHttpHeader(time);

        // signature
        String sig = initService.makeSignature(time,"POST", BASE_SMS_URL + serviceId + SEND_SMS_URL);
        headers.set("x-ncp-apigw-signature-v2", sig);

        // json body + json header
        return new HttpEntity<>(jsonBody, headers);
    }

    private HttpEntity<String> searchMessageResultToJson(String messageId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // header configuration
        String time = Long.toString(System.currentTimeMillis());
        HttpHeaders headers = getHttpHeader(time);

        // signature
        String sig = initService.makeSignature(time,"GET", BASE_SMS_URL + serviceId + SEARCH_MESSAGES_RESULT_URL + messageId);
        headers.set("x-ncp-apigw-signature-v2", sig);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<String> searchMessageRequestToJson(String requestId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // header configuration
        String time = Long.toString(System.currentTimeMillis());
        HttpHeaders headers = getHttpHeader(time);

        // signature
        String sig = initService.makeSignature(time,"GET", BASE_SMS_URL + serviceId + SEARCH_MESSAGES_REQUEST_URL + requestId);
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
