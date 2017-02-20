package com.wx.java.share.controller;

import com.wx.java.share.core.wx.exception.ErrorCode;
import com.wx.java.share.core.wx.exception.WxException;
import com.wx.java.share.core.wx.service.WxService;
import com.wx.java.share.entity.AccessToken;
import com.wx.java.share.entity.menu.Button;
import com.wx.java.share.entity.menu.CommonButton;
import com.wx.java.share.entity.menu.ComplexButton;
import com.wx.java.share.entity.menu.Menu;
import com.wx.java.share.service.MenuService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by clear on 2017/2/19.
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;
    @Autowired
    private WxService wxService;
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public int initMenu(){
        AccessToken accessToken = wxService.getAccessToken();
        if(accessToken == null){
            throw new WxException(ErrorCode.MENU_NOT_EXIST.getCode(),ErrorCode.MENU_NOT_EXIST.getMsg());
        }

        int result = menuService.createMenu(getMenu(),accessToken.getToken());
        if(result == 0){
            logger.info("创建菜单成功");
        }else{
            logger.error("创建菜单失败");
        }
        return result;
    }
//    @RequestMapping(value = "/get",method = RequestMethod.GET)
//    private String query(String token){
//        String result = "";
//        AccessToken accessToken = wxService.getAccessToken();
//        if(accessToken == null){
//            throw new WxException(ErrorCode.MENU_NOT_EXIST.getCode(),ErrorCode.MENU_NOT_EXIST.getMsg());
//        }
//        JSONObject object = menuService.queryMenu(accessToken.getToken());
//        if(object != null){
//            result = object.toString();
//        }
//        return result;
//    }

    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("慕课网");
        btn11.setType("view");
        btn11.setUrl("http://www.imooc.com");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("公交查询");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("周边搜索");
        btn13.setType("click");
        btn13.setKey("13");

        CommonButton btn14 = new CommonButton();
        btn14.setName("历史上的今天");
        btn14.setType("click");
        btn14.setKey("14");

        CommonButton btn21 = new CommonButton();
        btn21.setName("歌曲点播");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("经典游戏");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("美女电台");
        btn23.setType("click");
        btn23.setKey("23");

        CommonButton btn24 = new CommonButton();
        btn24.setName("人脸识别");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButton btn25 = new CommonButton();
        btn25.setName("聊天唠嗑");
        btn25.setType("click");
        btn25.setKey("25");

        CommonButton btn31 = new CommonButton();
        btn31.setName("Q友圈");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("电影排行榜");
        btn32.setType("click");
        btn32.setKey("32");

        CommonButton btn33 = new CommonButton();
        btn33.setName("幽默笑话");
        btn33.setType("view");
        btn33.setUrl("");
        btn33.setKey("33");

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("技术交流");
        mainBtn1.setSub_button(new CommonButton[] { btn11, btn12, btn13, btn14 });

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("休闲驿站");
        mainBtn2.setSub_button(new CommonButton[] { btn21, btn22, btn23, btn24, btn25 });

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("更多体验");
        mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33 });

        /**
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项

         *
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？

         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：

         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

        return menu;
    }
}
