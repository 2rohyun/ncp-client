package com.ncp.ncpclient.sens.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestUtil<T> {

    private T dto;

    public static <T> ResponseEntity<T> sendRequest(HttpEntity<String> requestToJson, String url, HttpMethod method) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), method, requestToJson, new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });
    }
}
