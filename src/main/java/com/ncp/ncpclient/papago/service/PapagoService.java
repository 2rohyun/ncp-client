package com.ncp.ncpclient.papago.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.common.api.InitService;
import com.ncp.ncpclient.common.utils.ApiRequestUtil;
import com.ncp.ncpclient.papago.dto.request.NmtRequestDto;
import com.ncp.ncpclient.papago.dto.response.NmtResponseDto;
import com.ncp.ncpclient.sens.dto.request.MessagesRequestDto;
import com.ncp.ncpclient.sens.dto.request.SmsRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ncp.ncpclient.common.utils.ApiRequestUtil.sendRequest;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:ncp.properties")
@Getter
public class PapagoService {

    @Value("${papago.clientId}")
    private String clientId;

    @Value("${papago.clientSecret}")
    private String clientSecret;

    private final String NMT_BASE_URL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";

    /**
     * @param source : Language before translation
     * @param target : Language after translation
     * @param text : Text you want to translate
     * @return : ResponseEntity<NmtResponseDto>
     */
    public ResponseEntity<NmtResponseDto> translation(String source, String target, String text)
            throws JsonProcessingException, URISyntaxException {
        NmtRequestDto nmtRequestDto = createNmtRequest(source, target, text);
        return sendRequest(NmtRequestToJson(nmtRequestDto),NMT_BASE_URL,HttpMethod.POST);
    }

    /**
     * @param source : Language before translation
     * @param target : Language after translation
     * @param text : Text you want to translate
     * @param honorific : Whether or not to speak of honor. This applies only to English -> Korean translations. Default false
     * @return ResponseEntity<NmtResponseDto>
     */
    public ResponseEntity<NmtResponseDto> translation(String source, String target, String text, boolean honorific)
            throws JsonProcessingException, URISyntaxException {
        NmtRequestDto nmtRequestDto = createNmtRequest(source, target, text, honorific);
        return sendRequest(NmtRequestToJson(nmtRequestDto),NMT_BASE_URL,HttpMethod.POST);
    }

    private HttpEntity<String> NmtRequestToJson(NmtRequestDto nmtRequestDto) throws JsonProcessingException {
        HttpHeaders headers = getHttpHeader();
        String jsonBody = getHttpBody(nmtRequestDto);
        return new HttpEntity<> (jsonBody, headers);
    }

    private NmtRequestDto createNmtRequest(String source, String target, String text) {
        return new NmtRequestDto(source, target, text);
    }

    private NmtRequestDto createNmtRequest(String source, String target, String text, boolean honorific) {
        return new NmtRequestDto(source, target, text, honorific);
    }

    private HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        return headers;
    }

    private String getHttpBody(NmtRequestDto nmtRequestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(nmtRequestDto);
    }

}
