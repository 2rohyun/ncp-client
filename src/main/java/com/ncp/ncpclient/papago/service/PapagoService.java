package com.ncp.ncpclient.papago.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncp.ncpclient.papago.dto.request.DetectionRequestDto;
import com.ncp.ncpclient.papago.dto.request.NmtRequestDto;
import com.ncp.ncpclient.papago.dto.response.DetectionResponseDto;
import com.ncp.ncpclient.papago.dto.response.KoreanNameRomanizerResponseDto;
import com.ncp.ncpclient.papago.dto.response.NmtResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.Utf8Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.ncp.ncpclient.common.utils.ApiRequestUtil.sendRequest;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.HttpMethod.*;

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

    private final String DETECTION_BASE_URL = "https://naveropenapi.apigw.ntruss.com/langs/v1/dect";

    private final String KOREAN_NAME_ROMANIZER_BASE_URL = "https://naveropenapi.apigw.ntruss.com/krdict/v1/romanization";

    private final String KOREAN_NAME_ROMANIZER_PLUS_URL = "?query=";

    /**
     * @param source : Language before translation
     * @param target : Language after translation
     * @param text : Text you want to translate
     * @return : ResponseEntity<NmtResponseDto>
     */
    public ResponseEntity<NmtResponseDto> translation(String source, String target, String text)
            throws JsonProcessingException, URISyntaxException {
        NmtRequestDto nmtRequestDto = createNmtRequest(source, target, text);
        return sendRequest(NmtRequestToJson(nmtRequestDto),
                            NMT_BASE_URL,
                            POST,
                            NmtResponseDto.class);
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
        return sendRequest(NmtRequestToJson(nmtRequestDto),
                            NMT_BASE_URL,
                            POST,
                            NmtResponseDto.class);
    }

    /**
     * @param text : Language you want to detect
     * @return : ResponseEntity<DetectionResponseDto>
     */
    public ResponseEntity<DetectionResponseDto> detection(String text)
            throws URISyntaxException, JsonProcessingException {
        DetectionRequestDto detectionRequestDto = createDetectionRequest(text);
        return sendRequest(DetectionRequestToJson(detectionRequestDto),
                            DETECTION_BASE_URL,
                            POST,
                            DetectionResponseDto.class);
    }

    /**
     * @param koreanName : Korean name to be changed to Roman characters
     * @return : ResponseEntity<KoreanNameRomanizerResponseDto>
     */
    public ResponseEntity<KoreanNameRomanizerResponseDto> romanization(String koreanName)
            throws URISyntaxException {
        String encodedName = encode(koreanName, UTF_8);
        System.out.println(RomanizationRequestToJson());
        return sendRequest(RomanizationRequestToJson(),
                        KOREAN_NAME_ROMANIZER_BASE_URL + KOREAN_NAME_ROMANIZER_PLUS_URL + encodedName,
                            GET,
                            KoreanNameRomanizerResponseDto.class);
    }

    private HttpEntity<String> NmtRequestToJson(NmtRequestDto nmtRequestDto)
            throws JsonProcessingException {
        HttpHeaders headers = getHttpHeader();
        String jsonBody = getHttpBody(nmtRequestDto);
        return new HttpEntity<> (jsonBody, headers);
    }

    private HttpEntity<String> DetectionRequestToJson(DetectionRequestDto detectionRequestDto)
            throws JsonProcessingException {
        HttpHeaders headers = getHttpHeader();
        String jsonBody = getHttpBody(detectionRequestDto);
        return new HttpEntity<> (jsonBody, headers);
    }

    private HttpEntity<String> RomanizationRequestToJson() {
        HttpHeaders headers = getHttpHeader();
        return new HttpEntity<>(headers);
    }

    private NmtRequestDto createNmtRequest(String source, String target, String text) {
        return new NmtRequestDto(source, target, text);
    }

    private NmtRequestDto createNmtRequest(String source, String target, String text, boolean honorific) {
        return new NmtRequestDto(source, target, text, honorific);
    }

    private DetectionRequestDto createDetectionRequest(String query) {
        return new DetectionRequestDto(query);
    }

    private HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", UTF_8);
        headers.setContentType(mediaType);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        return headers;
    }

    private String getHttpBody(NmtRequestDto nmtRequestDto)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(nmtRequestDto);
    }

    private String getHttpBody(DetectionRequestDto detectionRequestDto)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(detectionRequestDto);
    }

}
