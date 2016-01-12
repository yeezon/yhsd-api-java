package com.youhaosuda;


import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import java.util.Iterator;
import java.util.Map;


/**
 * Created by chenyg on 15/11/10.
 */
public class Api {
    private String token;
    private String url;
    private static Api instance = new Api();

    private Api() {
    }

    protected static Api getInstance(String token, String apiHost, String httpProtocol) {
        instance.token = token;
        instance.url = httpProtocol + "://" + apiHost + "/v1/";
        return instance;
    }

    /**
     * http get请求
     *
     * @param path 请求的路径
     * @return api返回的结果
     */
    public YhsdResponse get(String path) {
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(this.url + path);
        return Request.request(setHeader(requestBuilder, this.token));
    }

    /**
     * http get请求
     *
     * @param path 请求的路径
     * @param params 请求的参数
     * @return api返回的结果
     */
    public YhsdResponse get(String path, Map<String, String> params) {
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(this.url + path);
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            requestBuilder = requestBuilder.addParameter(key, params.get(key));
        }
        return Request.request(setHeader(requestBuilder, this.token));
    }

    /**
     * http post请求
     *
     * @param path 请求的路径
     * @param json 请求的json
     * @return api返回的结果
     */
    public YhsdResponse post(String path, String json) {
        StringEntity params = new StringEntity(json, "UTF-8");
        RequestBuilder requestBuilder = RequestBuilder.post().setUri(this.url + path).setEntity(params);
        return Request.request(setHeader(requestBuilder, this.token));
    }

    /**
     * http put请求
     *
     * @param path 请求的路径
     * @param json 请求的json
     * @return api返回的结果
     */
    public YhsdResponse put(String path, String json) {
        StringEntity params = new StringEntity(json, "UTF-8");
        RequestBuilder requestBuilder = RequestBuilder.put().setUri(this.url + path).setEntity(params);
        return Request.request(setHeader(requestBuilder, this.token));
    }

    /**
     * http delete请求
     *
     * @param path 请求的路径
     * @return api返回的结果
     */
    public YhsdResponse delete(String path) {
        RequestBuilder requestBuilder = RequestBuilder.delete().setUri(this.url + path);
        return Request.request(setHeader(requestBuilder, this.token));
    }

    private HttpUriRequest setHeader(RequestBuilder request, String token) {
        return request
                .setHeader("Content-Type", "application/json")
                .setHeader("X-API-ACCESS-TOKEN", token)
                .build();
    }
}
