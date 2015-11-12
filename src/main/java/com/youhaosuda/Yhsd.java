package com.youhaosuda;


/**
 * Created by chenyg on 15/11/11.
 */
public class Yhsd {
    private final String apiHost = "api.youhaosuda.com";
    private final String appHost = "apps.youhaosuda.com";
    private final String httpProtocol = "https";

    private String localApiHost;
    private String localAppHost;
    private String localHttpProtocol;

    private static Yhsd instance = new Yhsd();


    private Yhsd() {
    }

    /**
     * 使用默认配置,apiHost,appHost以及请求方式使用生产环境的地址
     */
    public static Yhsd getInstance() {
        instance.localApiHost = null;
        instance.localAppHost = null;
        instance.localHttpProtocol = null;
        return instance;
    }

    /**
     * 使用自定义配置,在调试时使用自定义的apiHost和appHost以及请求方式
     *
     * @param apiHost      api调用地址
     * @param appHost      app地址
     * @param httpProtocol 请求方式
     */
    public static Yhsd getInstance(String apiHost, String appHost, String httpProtocol) {
        instance.localApiHost = apiHost;
        instance.localAppHost = appHost;
        instance.localHttpProtocol = httpProtocol;
        return instance;
    }

    /**
     * 获得一个Api实例
     *
     * @param token 插件/应用的token,调用所有Api都需要使用到token
     * @return api的实例
     */
    public Api api(String token) {
        String apiHost = this.localApiHost == null ? this.apiHost : this.localApiHost;
        String httpProtocol = this.localHttpProtocol == null ? this.httpProtocol : this.localHttpProtocol;
        return Api.getInstance(token, apiHost, httpProtocol);
    }

    /**
     * 获得一个用于公有应用的Auth实例,
     *
     * @param appKey      插件/应用的appKey,可在合作伙伴后台获取
     * @param appSecret   插件/应用的appSecret,可在合作伙伴后台获取
     * @param redirectUrl 用于开放应用，接受code等参数的地址,详情见文档开放应用的第四步
     * @param scope       插件/应用的权限
     * @return Auth的实例
     */
    public Auth auth(String appKey, String appSecret, String redirectUrl, String[] scope) {
        String appHost = this.localAppHost == null ? this.appHost : this.localAppHost;
        String httpProtocol = this.localHttpProtocol == null ? this.httpProtocol : this.localHttpProtocol;
        return Auth.getInstance(appHost, httpProtocol, appKey, appSecret, redirectUrl, scope);
    }

    /**
     * 获得一个用于私有应用的Auth实例
     *
     * @param appKey      插件/应用的appKey,可在合作伙伴后台获取
     * @param appSecret   插件/应用的appSecret,可在合作伙伴后台获取
     * @param redirectUrl 用于开放应用，接受code等参数的地址,详情见文档开放应用的第四步
     * @return Auth的实例
     */
    public Auth auth(String appKey, String appSecret, String redirectUrl) {
        String appHost = this.localAppHost == null ? this.appHost : this.localAppHost;
        String httpProtocol = this.localHttpProtocol == null ? this.httpProtocol : this.localHttpProtocol;
        return Auth.getInstance(appHost, httpProtocol, appKey, appSecret, redirectUrl);
    }
}
