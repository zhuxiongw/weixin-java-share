package com.wx.java.share.core.wx.service;

import com.sun.org.apache.regexp.internal.RE;
import com.wx.java.share.core.wx.exception.ErrorCode;
import com.wx.java.share.core.wx.exception.WxException;
import com.wx.java.share.entity.AccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.weixin4j.Weixin;
import org.weixin4j.WeixinException;
import org.weixin4j.http.OAuthToken;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by clear on 2017/2/18.
 */
@Component
public class WxService {
    private static final Logger logger = LoggerFactory.getLogger(WxService.class);
    @Value("${wechat.token}")
    private String token;
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.appSecret}")
    private String appSecret;
    // 获取accesstoken url
    private static final  String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final  String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    @Autowired
    private CacheService cacheService;

    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = {token, timestamp, nonce};
        Arrays.sort(arr);//排序
        StringBuilder builder = new StringBuilder();
        for (String s : arr) {
            builder.append(s);
        }
        //加密
        String temp = DigestUtils.sha1Hex(builder.toString());
        return temp.equals(signature);

    }

    /**
     * 获取AccessToken
     *
     * @return
     */
  /*  public String getAccessToken() {
        String accessToken = cacheService.getAccessToken(this.appId);
        try {

            if (StringUtils.isBlank(accessToken)) {
                Weixin weixin = new Weixin();
                OAuthToken token = weixin.login(this.appId, this.appSecret);
                cacheService.saveAccessToken(token.getAccess_token(), this.appId);
                return token.getAccess_token();
            }
        } catch (WeixinException e) {
            e.printStackTrace();
            logger.error("获取accessToken失败!");
        }
        return accessToken;
    }*/

    /**
     * get请求
     * @param url
     * @return
     */
    public JSONObject doGetString(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        JSONObject json = null;

        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                String result = EntityUtils.toString(entity,"UTF-8");
                json = JSONObject.fromObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * post请求
     * @param url
     * @param output
     * @return
     */
    public JSONObject doPostString(String url,String output){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject json = null;

        try {
            post.setEntity(new StringEntity(output,"UTF-8"));
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                String result = EntityUtils.toString(entity,"UTF-8");
                json = JSONObject.fromObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    /**
     * 获取accessToken
     * @return
     */
    public AccessToken getAccessToken(){
        AccessToken token = new AccessToken();
        token = cacheService.getAccessToken(this.appId);
        if(token == null){
            String url = ACCESS_TOKEN_URL.replace("APPID",this.appId).replace("APPSECRET",this.appSecret);
            JSONObject json = this.doGetString(url);
            if(json != null){
                token.setToken(json.getString("access_token"));
                token.setExpiresIn(json.getInt("expires_in"));
            }
            return token;
        }else {
            return token;
        }
    }

    public String upload(String filePath, String accessToken, String type) throws IOException{
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if(!"image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }
}
