package com.wx.java.share.controller;

import com.google.gson.JsonObject;
import com.wx.java.share.core.wx.exception.WxException;
import com.wx.java.share.core.wx.service.WxService;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clear on 2017/2/18.
 */
@Controller
public class WxMenuController {
    private static final Logger logger = LoggerFactory.getLogger(WxMenuController.class);
    @Autowired
    private WxService wxService;

    private static final String CREATE_MENU_URL_PREFFOX = " https://api.weixin.qq.com/cgi-bin/menu/";


}
