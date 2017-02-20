//package com.wx.java.share.core.wx.service;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import me.chanjar.weixin.common.bean.WxJsapiSignature;
//import me.chanjar.weixin.common.bean.result.WxError;
//import me.chanjar.weixin.common.exception.WxErrorException;
//import me.chanjar.weixin.common.session.StandardSessionManager;
//import me.chanjar.weixin.common.session.WxSessionManager;
//import me.chanjar.weixin.common.util.RandomUtils;
//import me.chanjar.weixin.common.util.crypto.SHA1;
//import me.chanjar.weixin.common.util.http.*;
//import me.chanjar.weixin.mp.api.*;
//import me.chanjar.weixin.mp.api.impl.*;
//import me.chanjar.weixin.mp.bean.*;
//import me.chanjar.weixin.mp.bean.result.*;
//import org.apache.http.HttpHost;
//import org.apache.http.conn.ssl.DefaultHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * Created by clear on 2017/2/18.
// */
//@Component
//public class WxMpServiceImpl implements WxMpService {
//    private static  final Logger logger = LoggerFactory.getLogger(WxMpServiceImpl.class);
//
//    private static final JsonParser JSON_PARSER = new JsonParser();
//    protected final Logger log = LoggerFactory.getLogger(this.getClass());
//    private final Object globalAccessTokenRefreshLock = new Object();
//    private final Object globalJsapiTicketRefreshLock = new Object();
//    private WxMpConfigStorage configStorage;
//    private WxMpKefuService kefuService = new WxMpKefuServiceImpl(this);
//    private WxMpMaterialService materialService = new WxMpMaterialServiceImpl(this);
//    private WxMpMenuService menuService = new WxMpMenuServiceImpl(this);
//    private WxMpUserService userService = new WxMpUserServiceImpl(this);
//    private WxMpUserTagService tagService = new WxMpUserTagServiceImpl(this);
//    private WxMpQrcodeService qrCodeService = new WxMpQrcodeServiceImpl(this);
//    private WxMpCardService cardService = new WxMpCardServiceImpl(this);
//    private WxMpPayService payService = new WxMpPayServiceImpl(this);
//    private WxMpStoreService storeService = new WxMpStoreServiceImpl(this);
//    private WxMpDataCubeService dataCubeService = new WxMpDataCubeServiceImpl(this);
//    private WxMpUserBlacklistService blackListService = new WxMpUserBlacklistServiceImpl(this);
//    private WxMpTemplateMsgService templateMsgService = new WxMpTemplateMsgServiceImpl(this);
//    private CloseableHttpClient httpClient;
//    private HttpHost httpProxy;
//    private int retrySleepMillis = 1000;
//    private int maxRetryTimes = 5;
//    protected WxSessionManager sessionManager = new StandardSessionManager();
//
//    @Override
//    public String getAccessToken(boolean b) throws WxErrorException {
//        if(b){
//            this.configStorage.getAccessToken();
//        }
//
//
//        return this.configStorage.getAccessToken();
//    }
//
//    @Override
//    public String getJsapiTicket(boolean b) throws WxErrorException {
//        return null;
//    }
//
//
//    public boolean checkSignature(String timestamp, String nonce, String signature) {
//        try {
//            logger.debug("token:{}-------{}",signature,this.configStorage.getToken());
//            return SHA1.gen(new String[]{this.configStorage.getToken(), timestamp, nonce}).equals(signature);
//        } catch (Exception var5) {
//            return false;
//        }
//    }
//
//    public String getAccessToken() throws WxErrorException {
//        return this.getAccessToken(false);
//    }
//
//
//
//
//    public String getJsapiTicket() throws WxErrorException {
//        return this.getJsapiTicket(false);
//    }
//
//
//
//    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
//        long timestamp = System.currentTimeMillis() / 1000L;
//        String noncestr = RandomUtils.getRandomStr();
//        String jsapiTicket = this.getJsapiTicket(false);
//        String signature = SHA1.genWithAmple(new String[]{"jsapi_ticket=" + jsapiTicket, "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url});
//        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
//        jsapiSignature.setAppid(this.configStorage.getAppId());
//        jsapiSignature.setTimestamp(timestamp);
//        jsapiSignature.setNoncestr(noncestr);
//        jsapiSignature.setUrl(url);
//        jsapiSignature.setSignature(signature);
//        return jsapiSignature;
//    }
//
//    public WxMpMassUploadResult massNewsUpload(WxMpMassNews news) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
//        String responseContent = this.post(url, news.toJson());
//        return WxMpMassUploadResult.fromJson(responseContent);
//    }
//
//    public WxMpMassUploadResult massVideoUpload(WxMpMassVideo video) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadvideo";
//        String responseContent = this.post(url, video.toJson());
//        return WxMpMassUploadResult.fromJson(responseContent);
//    }
//
//    public WxMpMassSendResult massGroupMessageSend(WxMpMassTagMessage message) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
//        String responseContent = this.post(url, message.toJson());
//        return WxMpMassSendResult.fromJson(responseContent);
//    }
//
//    public WxMpMassSendResult massOpenIdsMessageSend(WxMpMassOpenIdsMessage message) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
//        String responseContent = this.post(url, message.toJson());
//        return WxMpMassSendResult.fromJson(responseContent);
//    }
//
//    public WxMpMassSendResult massMessagePreview(WxMpMassPreviewMessage wxMpMassPreviewMessage) throws Exception {
//        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview";
//        String responseContent = this.post(url, wxMpMassPreviewMessage.toJson());
//        return WxMpMassSendResult.fromJson(responseContent);
//    }
//
//    public String shortUrl(String long_url) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
//        JsonObject o = new JsonObject();
//        o.addProperty("action", "long2short");
//        o.addProperty("long_url", long_url);
//        String responseContent = this.post(url, o.toString());
//        JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
//        return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
//    }
//
//    public WxMpSemanticQueryResult semanticQuery(WxMpSemanticQuery semanticQuery) throws WxErrorException {
//        String url = "https://api.weixin.qq.com/semantic/semproxy/search";
//        String responseContent = this.post(url, semanticQuery.toJson());
//        return WxMpSemanticQueryResult.fromJson(responseContent);
//    }
//
//    public String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state) {
//        StringBuilder url = new StringBuilder();
//        url.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
//        url.append("appid=").append(this.configStorage.getAppId());
//        url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
//        url.append("&response_type=code");
//        url.append("&scope=").append(scope);
//        if(state != null) {
//            url.append("&state=").append(state);
//        }
//
//        url.append("#wechat_redirect");
//        return url.toString();
//    }
//
//    public String buildQrConnectUrl(String redirectURI, String scope, String state) {
//        StringBuilder url = new StringBuilder();
//        url.append("https://open.weixin.qq.com/connect/qrconnect?");
//        url.append("appid=").append(this.configStorage.getAppId());
//        url.append("&redirect_uri=").append(URIUtil.encodeURIComponent(redirectURI));
//        url.append("&response_type=code");
//        url.append("&scope=").append(scope);
//        if(state != null) {
//            url.append("&state=").append(state);
//        }
//
//        url.append("#wechat_redirect");
//        return url.toString();
//    }
//
//    private WxMpOAuth2AccessToken getOAuth2AccessToken(StringBuilder url) throws WxErrorException {
//        try {
//            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
//            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url.toString(), null);
//            return WxMpOAuth2AccessToken.fromJson(responseText);
//        } catch (IOException var4) {
//            throw new RuntimeException(var4);
//        }
//    }
//
//    public WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException {
//        StringBuilder url = new StringBuilder();
//        url.append("https://api.weixin.qq.com/sns/oauth2/access_token?");
//        url.append("appid=").append(this.configStorage.getAppId());
//        url.append("&secret=").append(this.configStorage.getSecret());
//        url.append("&code=").append(code);
//        url.append("&grant_type=authorization_code");
//        return this.getOAuth2AccessToken(url);
//    }
//
//    public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WxErrorException {
//        StringBuilder url = new StringBuilder();
//        url.append("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
//        url.append("appid=").append(this.configStorage.getAppId());
//        url.append("&grant_type=refresh_token");
//        url.append("&refresh_token=").append(refreshToken);
//        return this.getOAuth2AccessToken(url);
//    }
//
//    public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException {
//        StringBuilder url = new StringBuilder();
//        url.append("https://api.weixin.qq.com/sns/userinfo?");
//        url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
//        url.append("&openid=").append(oAuth2AccessToken.getOpenId());
//        if(lang == null) {
//            url.append("&lang=zh_CN");
//        } else {
//            url.append("&lang=").append(lang);
//        }
//
//        try {
//            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
//            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url.toString(), null);
//            return WxMpUser.fromJson(responseText);
//        } catch (IOException var6) {
//            throw new RuntimeException(var6);
//        }
//    }
//
//    public boolean oauth2validateAccessToken(WxMpOAuth2AccessToken oAuth2AccessToken) {
//        StringBuilder url = new StringBuilder();
//        url.append("https://api.weixin.qq.com/sns/auth?");
//        url.append("access_token=").append(oAuth2AccessToken.getAccessToken());
//        url.append("&openid=").append(oAuth2AccessToken.getOpenId());
//
//        try {
//            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
//            e.execute(this.getHttpclient(), this.httpProxy, url.toString(), null);
//            return true;
//        } catch (IOException var4) {
//            throw new RuntimeException(var4);
//        } catch (WxErrorException var5) {
//            return false;
//        }
//    }
//
//    public String[] getCallbackIP() throws WxErrorException {
//        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
//        String responseContent = this.get(url, (String)null);
//        JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
//        JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
//        String[] ipArray = new String[ipList.size()];
//
//        for(int i = 0; i < ipList.size(); ++i) {
//            ipArray[i] = ipList.get(i).getAsString();
//        }
//
//        return ipArray;
//    }
//
//    public String get(String url, String queryParam) throws WxErrorException {
//        return (String)this.execute(new SimpleGetRequestExecutor(), url, queryParam);
//    }
//
//    public String post(String url, String postData) throws WxErrorException {
//        return (String)this.execute(new SimplePostRequestExecutor(), url, postData);
//    }
//
//    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
//        int retryTimes = 0;
//
//        while(true) {
//            try {
//                T e = this.executeInternal(executor, uri, data);
//                this.log.debug("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", new Object[]{uri, data, e});
//                return e;
//            } catch (WxErrorException var10) {
//                WxError error = var10.getError();
//                if(error.getErrorCode() != -1) {
//                    throw var10;
//                }
//
//                int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
//
//                try {
//                    this.log.debug("微信系统繁忙，{}ms 后重试(第{}次)", Integer.valueOf(sleepMillis), Integer.valueOf(retryTimes + 1));
//                    Thread.sleep((long)sleepMillis);
//                } catch (InterruptedException var9) {
//                    throw new RuntimeException(var9);
//                }
//
//                ++retryTimes;
//                if(retryTimes >= this.maxRetryTimes) {
//                    throw new RuntimeException("微信服务端异常，超出重试次数");
//                }
//            }
//        }
//    }
//
//    protected synchronized <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
//        if(uri.indexOf("access_token=") != -1) {
//            throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
//        } else {
//            String accessToken = this.getAccessToken(false);
//            String uriWithAccessToken = uri + (uri.indexOf(63) == -1?"?access_token=" + accessToken:"&access_token=" + accessToken);
//
//            try {
//                return executor.execute(this.getHttpclient(), this.httpProxy, uriWithAccessToken, data);
//            } catch (WxErrorException var8) {
//                WxError error = var8.getError();
//                if(error.getErrorCode() != 'ꐑ' && error.getErrorCode() != '鱁') {
//                    if(error.getErrorCode() != 0) {
//                        this.log.error("\n[URL]:  {}\n[PARAMS]: {}\n[RESPONSE]: {}", new Object[]{uri, data, error});
//                        throw new WxErrorException(error);
//                    } else {
//                        return null;
//                    }
//                } else {
//                    this.configStorage.expireAccessToken();
//                    return this.execute(executor, uri, data);
//                }
//            } catch (IOException var9) {
//                this.log.error("\n[URL]:  {}\n[PARAMS]: {}\n[EXCEPTION]: {}", new Object[]{uri, data, var9.getMessage()});
//                throw new RuntimeException(var9);
//            }
//        }
//    }
//
//    public HttpHost getHttpProxy() {
//        return this.httpProxy;
//    }
//
//    public CloseableHttpClient getHttpclient() {
//        return this.httpClient;
//    }
//
//    public void setWxMpConfigStorage(WxMpConfigStorage wxConfigProvider) {
//        this.configStorage = wxConfigProvider;
//        this.initHttpClient();
//    }
//
//    private void initHttpClient() {
//        Object apacheHttpClientBuilder = this.configStorage.getApacheHttpClientBuilder();
//        if(null == apacheHttpClientBuilder) {
//            apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
//        }
//
//        ((ApacheHttpClientBuilder)apacheHttpClientBuilder).httpProxyHost(this.configStorage.getHttpProxyHost()).httpProxyPort(this.configStorage.getHttpProxyPort()).httpProxyUsername(this.configStorage.getHttpProxyUsername()).httpProxyPassword(this.configStorage.getHttpProxyPassword());
//        if(this.configStorage.getSSLContext() != null) {
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(this.configStorage.getSSLContext(), new String[]{"TLSv1"}, (String[])null, new DefaultHostnameVerifier());
//            ((ApacheHttpClientBuilder)apacheHttpClientBuilder).sslConnectionSocketFactory(sslsf);
//        }
//
//        if(this.configStorage.getHttpProxyHost() != null && this.configStorage.getHttpProxyPort() > 0) {
//            this.httpProxy = new HttpHost(this.configStorage.getHttpProxyHost(), this.configStorage.getHttpProxyPort());
//        }
//
//        this.httpClient = ((ApacheHttpClientBuilder)apacheHttpClientBuilder).build();
//    }
//
//    public WxMpConfigStorage getWxMpConfigStorage() {
//        return this.configStorage;
//    }
//
//    public void setRetrySleepMillis(int retrySleepMillis) {
//        this.retrySleepMillis = retrySleepMillis;
//    }
//
//    public void setMaxRetryTimes(int maxRetryTimes) {
//        this.maxRetryTimes = maxRetryTimes;
//    }
//
//    public WxMpKefuService getKefuService() {
//        return this.kefuService;
//    }
//
//    public WxMpMaterialService getMaterialService() {
//        return this.materialService;
//    }
//
//    public WxMpMenuService getMenuService() {
//        return this.menuService;
//    }
//
//    public WxMpUserService getUserService() {
//        return this.userService;
//    }
//
//    public WxMpUserTagService getUserTagService() {
//        return this.tagService;
//    }
//
//    public WxMpQrcodeService getQrcodeService() {
//        return this.qrCodeService;
//    }
//
//    public WxMpCardService getCardService() {
//        return this.cardService;
//    }
//
//    public WxMpPayService getPayService() {
//        return this.payService;
//    }
//
//    public WxMpDataCubeService getDataCubeService() {
//        return this.dataCubeService;
//    }
//
//    public WxMpUserBlacklistService getBlackListService() {
//        return this.blackListService;
//    }
//
//    public WxMpStoreService getStoreService() {
//        return this.storeService;
//    }
//
//    public WxMpTemplateMsgService getTemplateMsgService() {
//        return this.templateMsgService;
//    }
//
//
//}
