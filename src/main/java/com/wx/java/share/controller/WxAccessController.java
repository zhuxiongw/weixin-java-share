package com.wx.java.share.controller;

import com.wx.java.share.core.wx.service.WxService;
import com.wx.java.share.service.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by clear on 2017/2/18.
 */
@RestController
@RequestMapping("/wx/zxw/java")
public class WxAccessController {
    private static final Logger logger = LoggerFactory.getLogger(WxAccessController.class);
    @Autowired
    private WxService wxService;
    @Autowired
    private CoreService coreService;

    @RequestMapping(value = "endpoint", method = RequestMethod.GET)
    @ResponseBody
    public String wxAccess(String signature, String timestamp, String nonce, String echostr) {
        logger.info("{signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",echostr:" + echostr + "}");
        if (!wxService.checkSignature(signature, timestamp, nonce)) {
            logger.error("接入失败!");
            return "error request";
        }
        return echostr;
    }

    @RequestMapping(value = "endpoint", method = RequestMethod.POST)
    public String hanndleWxRequest(HttpServletRequest request) {
        logger.info("进入处理微信请求方法");
        String message = coreService.processRequest(request);
        logger.info("result:{}",message);
        return message;
    }
}
