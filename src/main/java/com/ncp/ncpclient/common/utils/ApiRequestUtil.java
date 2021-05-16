package com.ncp.ncpclient.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;


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
    public static <T> ResponseEntity<T> sendRequest(HttpEntity<String> requestToJson,
                                                    String url,
                                                    HttpMethod method,
                                                    Class<T> clazz) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(new URI(url), method, requestToJson, clazz);
    }
}
