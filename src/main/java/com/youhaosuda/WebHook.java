package com.youhaosuda;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by chenyg on 16/1/12.
 */
public class WebHook {
    private String webHookToken;
    private static WebHook instance = new WebHook();

    protected static WebHook getInstance(String webHookToken) {
        instance.webHookToken = webHookToken;

        return instance;
    }

    private WebHook() {
    }

    public boolean verifyHmac(String hmac, String responseBody) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey macKey = new SecretKeySpec(instance.webHookToken.getBytes(), "HmacSHA256");
        Mac mac;
        mac = Mac.getInstance("HmacSHA256");
        mac.init(macKey);
        byte[] bytes = mac.doFinal(responseBody.getBytes());
        return Util.byteArrayToHexString(bytes).equals(hmac);

    }
}
