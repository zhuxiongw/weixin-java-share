package com.wx.java.share.core.wx.exception;

/**
 * Created by clear on 2017/2/18.
 */
public enum  ErrorCode {
    SUCCESS(1000,"成功"),

    ENDPOINT_ERROT(1001,"无入口权限"),
    FILE_NOT_EXIST(1002,"文件不存在"),
    MENU_NOT_EXIST(1003,"创建菜单失败"),
    SERVICE_TYPE_ERROR(1004,"服务类型错误"),
    QUEUE_STATUS_ERROR(1005,"排队状态错误"),
    ALREADY_EXISTS(1006,"已经存在"),
    CALL_NUM_MAX(1007,"达到通知最大次数"),
    NOT_AUTH(1008,"没有权限"),
    PARAM_EMPTY(1009,"参数为空"),
    PARAM_ERROR(1010,"参数错误"),
    ALREADY_LOGOUT_COUNTER(1011,"已经退出柜台"),
    ALREADY_LOGIN_COUNTER(1012,"已经登录柜台"),
    NO_LOGIN(1013,"未登录"),
    PWD_ERROR(1014,"密码错误"),
    NO_COUNTER(1015,"没有柜台"),
    NOT_OFFICE_HOURS(1016,"不在营业时间"),
    NO_LOCATION(1017,"没有坐标"),
    LOCATION_ERROR(1018,"位置错误"),
    TOKEN_ERROR(1019,"token错误"),
    TOKEN_EXPIRE(1020,"token过期"),
    EMPLOYEE_UNVALID(1021,"柜员已不可用"),
    SERVCE_ERROR(1022,"服务错误"),
    WX_TMPL_MSG_SEND_ERROR(1023,"微信模板消息发送错误"),
            ;


    int code;
    String msg;

    ErrorCode(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
