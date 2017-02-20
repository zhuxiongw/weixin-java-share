package com.wx.java.share.service.impl;

import com.wx.java.share.core.wx.service.WxService;
import com.wx.java.share.entity.AccessToken;
import com.wx.java.share.entity.menu.Menu;
import com.wx.java.share.service.MenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜单业务类
 * Created by clear on 2017/2/19.
 */
@Service
public class MenuServiceImpl implements MenuService{
    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private WxService wxService;
    public static final  String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    public JSONObject queryMenu(String token) {
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = wxService.doGetString(url);
        return jsonObject;
    }

    @Override
    public int createMenu(Menu menu, String accessToken) {
        int result = 0;
        String url = MENU_CREATE_URL.replace("ACCESS_TOKEN",accessToken);
        // 将菜单对象转换成JSON
        String menuJson = JSONObject.fromObject(menu).toString();

        JSONObject menuObject = wxService.doPostString(url,menuJson);
        if (menuObject != null){
            if(menuObject.getInt("menuObject") != 1){
                result = menuObject.getInt("menuObject");
                logger.debug("创建菜单失败，返回参数：{}",menuObject.toString());
            }
        }
        return result;
    }

}
