package com.youhaosuda;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


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

    public boolean verifyHmac(String hmac, String responseBody) {
        SecretKey macKey = new SecretKeySpec(instance.webHookToken.getBytes(), "HmacSHA256");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(macKey);
            byte[] bytes = mac.doFinal(responseBody.getBytes());
            return Util.byteArrayToHexString(bytes).equals(hmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
