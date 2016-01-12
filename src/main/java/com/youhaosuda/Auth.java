package com.youhaosuda;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.net.URLEncoder;


/**
 * Created by chenyg on 15/11/10.
 */
public class Auth {
    private String appKey;
    private String appSecret;
    private String appRedirectUrl = "";
    private String[] scope = {"read_basic"};
    private String appHost = "apps.youhaosuda.com";
    private String httpProtocol = "https";
    private Mac mac;

    private Cipher cipher;
    private final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    private Key secretKey;
    private IvParameterSpec iv;

    private static Auth instance = new Auth();


    private Auth() {
    }

    protected static Auth getInstance(String appHost, String httpProtocol, String appKey, String appSecret, String redirectUrl, String[] scope) {
        instance.appHost = appHost;
        instance.httpProtocol = httpProtocol;
        instance.appKey = appKey;
        instance.appSecret = appSecret;
        instance.appRedirectUrl = redirectUrl;
        if (scope.length > 0) {
            instance.scope = scope;
        }
        SecretKey macKey = new SecretKeySpec(instance.appSecret.getBytes(), "HmacSHA256");
        try {
            instance.mac = Mac.getInstance("HmacSHA256");
            instance.mac.init(macKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return instance;
    }

    protected static Auth getInstance(String appHost, String httpProtocol, String appKey, String appSecret, String redirectUrl) {
        instance.appHost = appHost;
        instance.httpProtocol = httpProtocol;
        instance.appKey = appKey;
        instance.appSecret = appSecret;
        instance.appRedirectUrl = redirectUrl;
        SecretKey macKey = new SecretKeySpec(instance.appSecret.getBytes(), "HmacSHA256");
        try {
            instance.mac = Mac.getInstance("HmacSHA256");
            instance.mac.init(macKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 验证请求合法性
     *
     * @param hmac     请求中的hmac参数
     * @param urlParam 请求中的其它参数
     * @return boolean
     */
    public boolean verifyHmac(String hmac, String urlParam) {
        byte[] bytes = this.mac.doFinal(urlParam.getBytes());
        return Util.byteArrayToHexString(bytes).equals(hmac);
    }

    /**
     * 获取授权地址，用于开放应用
     *
     * @param shopKey shopKey,可在请求中获取
     * @param state   同上
     * @return authorizeUrl
     */
    public String getAuthorizeUrl(String shopKey, String state) {
        String scopeString = getScope(this.scope);
        String url = this.httpProtocol + "://" + this.appHost + "/oauth2/authorize?";
        String params = "response_type=code&client_id=" + this.appKey;
        params += "&shop_key=" + shopKey;
        params += "&scope=" + scopeString;
        params += "&state=" + state;
        params += "&redirect_uri=" + this.appRedirectUrl;
        try {
            return URLEncoder.encode(url + params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取授权地址，用于私有应用
     *
     * @param shopKey shopKey,可在请求中获取
     * @return authorizeUrl
     */
    public String getAuthorizeUrl(String shopKey) {
        String url = this.httpProtocol + "://" + this.appHost + "/oauth2/authorize?";
        String params = "response_type=code&client_id=" + this.appKey;
        params += "&shop_key=" + shopKey;
        params += "&redirect_uri=" + this.appRedirectUrl;
        try {
            return URLEncoder.encode(url + params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公有应用获取token
     *
     * @param code 可在请求中获取
     * @return token
     */
    public YhsdResponse getToken(String code) {
        RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(this.httpProtocol + "://" + this.appHost + "/oauth2/token")
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .addParameter("grant_type", "authorization_code")
                .addParameter("code", code)
                .addParameter("client_id", this.appKey)
                .addParameter("redirect_uri", this.appRedirectUrl);
        HttpUriRequest request = requestBuilder.build();
        return Request.request(request);

    }

    /**
     * 私有应用获取token
     *
     * @return token
     * @throws IOException
     */
    public YhsdResponse getToken() {
        RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri(this.httpProtocol + "://" + this.appHost + "/oauth2/token")
                .setHeader("Content-Type", "application/x-www-form-urlencoded");
        String auth = "Basic ";
        try {
            auth += new BASE64Encoder().encode((this.appKey + ":" + this.appSecret).getBytes("UTF-8")).replaceAll("\\s", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestBuilder = requestBuilder.setHeader("Authorization", auth)
                .addParameter("grant_type", "client_credentials");
        HttpUriRequest request = requestBuilder.build();
        return Request.request(request);
    }

    /**
     * 第三方接入支持
     *
     * @param customerData
     * @param strKey
     * @return
     * @throws Exception
     */
    public String thirdAppAesEncrypt(String customerData, String strKey) throws Exception {
        return aesEncrypt(strKey, customerData);
    }


    private String getScope(String[] scopeArray) {
        String scopeString = scopeArray[0];
        if (scopeArray.length > 1) {
            for (int i = 0; i < scopeArray.length; i++) {
                scopeString += scopeArray[i];
                if (i != scopeArray.length - 1) {
                    scopeString += ",";
                }
            }
        }
        return scopeString;
    }


    /**
     * 使用AES 算法 加密，默认模式 AES/CBC/PKCS5Padding
     */
    private String aesEncrypt(String strKey, String strData) throws Exception {
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        secretKey = getKey(strKey);
        iv = getIV(strKey);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypt = cipher.doFinal(strData.getBytes());
        return urlSafeBase64(new BASE64Encoder().encode(encrypt));
    }

    private IvParameterSpec getIV(String strIv) throws UnsupportedEncodingException {
        byte[] arrBTmp = strIv.getBytes("UTF-8");
        byte[] arrB = new byte[16];
        for (int i = 0; i + 16 < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i + 16];
        }
        return new IvParameterSpec(arrB);
    }

    private Key getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes("UTF-8");
        byte[] arrB = new byte[16];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new SecretKeySpec(arrB, "AES");
    }

    private String urlSafeBase64(String source) throws Exception {
        return source.replace("+", "-").replace("/", "_");
    }
}
