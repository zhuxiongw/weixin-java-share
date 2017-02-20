package com.wx.java.share.service;

import com.wx.java.share.entity.menu.Menu;
import net.sf.json.JSONObject;

/**
 * Created by clear on 2017/2/19.
 */
public interface MenuService {
    /**
     * 创建菜单
     * @param menu          菜单
     * @param accessToken   accessToken,微信唯一票据
     * @return
     */
    int createMenu(Menu menu,String accessToken);

    JSONObject queryMenu(String token);
}
