package com.youhaosuda;

import java.util.Map;

/**
 * Created by chenyg on 15/11/12.
 */
public class YhsdResponse {
    private String body;
    private int statusCode;
    private Map<String,String> header;

    protected YhsdResponse(String body, int statusCode, Map<String, String> header) {
        this.body = body;
        this.statusCode = statusCode;
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
