package com.wx.java.share.core.wx.service;

import com.wx.java.share.core.wx.utils.Contants;
import com.wx.java.share.entity.AccessToken;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Created by clear on 2017/2/18.
 */
@Component
public class CacheService {
    @Autowired
    private CacheManager cacheManager;

    /**
     * 保存微信accessToken到redis中
     * @param accessToken
     * @param appId
     */
    public void saveAccessToken(AccessToken accessToken, String appId){
        Cache cache =cacheManager.getCache(Contants.WEIXIN_ACCESS_TOKEN);
        cache.put(Contants.WEIXIN_ACCESS_TOKEN+appId,accessToken);
    }

    public AccessToken getAccessToken(String appId){
        AccessToken token = new AccessToken();
        Cache cache = cacheManager.getCache(Contants.WEIXIN_ACCESS_TOKEN);
        Cache.ValueWrapper value = cache.get(Contants.WEIXIN_ACCESS_TOKEN+appId);
        if(value == null)
            return null;
        JSONObject json = JSONObject.fromObject(value);
        token.setToken(json.getString("token"));
        token.setExpiresIn(json.getInt("expiresIn"));
        return token;
    }
}
