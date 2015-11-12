# yhsd-api-java

友好速搭 API SDK for Java

## 安装

等待上传至maven,在此之前,你可以[下载](https://github.com/yeezon/yhsd-api-java/releases/download/1.0/YhsdSdk.jar){:target="_blank"}我们已经打包好的jar

## 介绍
```java
/**
 * 用于授权和验证
 */
Auth auth;
/**
 * 用于调用各个Api
 */
Api api;
/**
 * api返回的结果
 * @method getHeader @return {Map<String,String>} http header
 * @method getBody @return {String} http body
 * @method getStatusCode @return {int} http status code
 */
 YhsdResponse response;
```

## 授权

```java
/**
 * 获得一个Auth实例,
 *
 * @param {String} appKey      插件/应用的appKey,可在合作伙伴后台获取
 * @param {String} appSecret   插件/应用的appSecret,可在合作伙伴后台获取
 * @param {String} redirectUrl 用于开放应用，接受code等参数的地址,详情见文档开放应用的第四步
 * @param {String[]} scope       用于公有插件/应用,表示权限 详情见https://docs.youhaosuda.com/app/553e333a0abc3e6f3e00001f
 * @return {Auth} Auth的实例
 */
String appKey = "your app key";
String appSecret = "your app secret";
String redirectUrl = "your redirectUrl";
String[] scope = {"read_basic"};
Auth auth = Yhsd.getInstance().auth(appkey,appSecret,redirectUrl);//私有
auth = Yhsd.getInstance().auth(appkey,appSecret,redirectUrl,scope);//公有

/**
 * 验证请求合法性
 *
 * @param {String} hmac  请求中的hmac参数
 * @param {String} param 请求中的其它参数  account_id=1&code=a84a110d86d2452eb3e2af4cfb8a3828&shop_key=a94a110d86d2452eb3e2af4cfb8a3828&time_stamp=2013-08-27T13:58:35Z
 * @return {boolean} 验证结果
 */
auth.verifyHmac(hmac,param);

/**
 * 获取授权地址，用于开放应用
 * 
 *
 * @param {String} shopKey shopKey,可在请求中获取
 * @param {String} state   可选，若包含此参数，则在用户授权后跳转，将这个参数返回
 * @return {String} 授权地址
 */
auth.getAuthorizeUrl(shopKey, state);

/**
 * 获取token
 *
 * @param {String} code 公有应用需要填写此参数 可在请求中获取,详情见官网更多=>文档中心=>应用开发=>开放API=>获取授权=>第四步
 * @return {YhsdResponse} token
 * 
 */
auth.getToken(code);
```

详见 
https://docs.youhaosuda.com/app/s/553e33880abc3e6f3e000026

### 例子

```java
String appKey = "your app key";
String appSecret = "your app secret";
String redirectUrl = "your redirectUrl";
String[] scope = {"read_basic"}; //私有应用不需要此项参数

Auth privateAppAuth = Yhsd.getInstance().auth(appkey,appSecret,redirectUrl); //私有
Auth publicAppAuth = Yhsd.getInstance().auth(appkey,appSecret,redirectUrl,scope); //公有

YhsdResponse privateTokenResp = privateAppAuth.getToken(); //私有应用
privateTokenResp.getBody();

String code = "your code";
YhsdResponse publicTokenResp  = publicAppAuth.getToken(code) ;//公有应用
publicTokenResp.getBody();
```

## 使用 API

```java
/**
 * 初始化
 * @param {String}  token
 * @return {Api} Api的实例
 */
String token = "your token";
Api api = Yhsd.getInstance().api(token);

/**
 * 发送GET请求
 * @param {String} path 请求的路径
 * @param {Map} param 可选,请求的参数
 * @return {YhsdResponse} api返回的结果
 *
 */
api.get(path);
api.get(path, param);

/**
 * 发送 PUT 请求
 * @param {String} path 请求的路径
 * @param {String} json 请求的json字符串
 * @return {YhsdResponse} api返回的结果
 */
api.put(path, json);

/**
 * 发送 POST 请求
 * @param {String} path 请求的路径
 * @param {String} json 请求的json字符串
 * @return {YhsdResponse} api返回的结果
 */
api.post(path, json);

/**
 * 发送 DELETE 请求
 * @param {String} path 请求的路径
 * @return {YhsdResponse} api返回的结果
 */
api.delete(path);
```

详见 
https://docs.youhaosuda.com/app/553e335f0abc3e6f3e000023

### 例子

```java
String token = "your token";
Api api = Yhsd.getInstance().api(token);
// 获取顾客列表
Map<String,String> param = new HashMap();
map.put("fields","id,name");
YhsdResponse resp = api.get('customers', param);
Map header = resp.getHeader()//api返回的header
int statusCode = resp.getStatusCode()//api返回的http StatusCode
String body = resp.getBody()//api返回的header
// 获取指定顾客
resp = api.get('customers/100');
Map header = resp.getHeader();
int statusCode = resp.getStatusCode();
String body = resp.getBody();
```

## 本地调试

```java
String apiAddr = "your local api server address"; 
String appAddr = "your local app server address"; 
String httpProtocol = "your http protocol";
Yhsd yhsd = Yhsd.getInstance(apiAddr,appAddr,httpProtocol);//api,auth调用方式不变
```
