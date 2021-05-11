package com.ncp.ncpclient.common.utils;

import com.ncp.ncpclient.sens.exception.SensException;
import com.ncp.ncpclient.sens.exception.SensExceptionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestUtil<T> {

    private T dto;

    /**
     * @param requestToJson : HttpEntity form of request to send
     * @param url : The url you want to send the request to
     * @param method : The http method depends on request type
     * @param <T> : dto generic type
     * @return : ResponseEntity<T>
     */
    public static <T> ResponseEntity<T> sendRequest(HttpEntity<String> requestToJson, String url, HttpMethod method) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
//        if (!validateURL(url)) {
//            throw new SensException(SensExceptionType.INVALID_URL);
//        }
        return restTemplate.exchange(new URI(url), method, requestToJson, new ParameterizedTypeReference<>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }

    //TODO ( fix regexp or change exception throw logic )
    private static boolean validateURL(String url) {
        return Pattern.matches("/(http|https):\\/\\/(\\w+:?\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?/", url);
    }
}
