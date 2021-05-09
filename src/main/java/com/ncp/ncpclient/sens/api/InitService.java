package com.ncp.ncpclient.sens.api;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@PropertySource("classpath:ncp.properties")
public class InitService {

    @Value("${sms.serviceId}")
    private String serviceId;

    @Value("${api.accessKey}")
    private String access;

    @Value("${api.secretKey}")
    private String secret;

    public String makeSignature(String time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/" + serviceId + "/messages"; // url (include query string)
        String accessKey = access;	// access key id (from portal or Sub Account)
        String secretKey = secret;

        String message =
                method +
                space +
                url +
                newLine +
                time +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

        return Base64.encodeBase64String(rawHmac);
    }
}
