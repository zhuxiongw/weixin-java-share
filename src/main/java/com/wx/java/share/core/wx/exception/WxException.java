package com.wx.java.share.core.wx.exception;

/**
 * Created by clear on 2017/2/18.
 */
public class WxException extends  RuntimeException{
    private static final long serialVersionUID = 71134982560437988L;

    private int errCode = 0;

    private String errMsg;

    public WxException(){
        super();
    }

    public WxException(int errCode){
        super("errCode:"+errCode);
        this.errCode = errCode;
    }

    public WxException(String errMsg){
        super(errMsg);
        this.errMsg = errMsg;
    }
    public WxException(ErrorCode errorCode){
        super(errorCode.getMsg());
        this.errCode = errorCode.getCode();
        this.errMsg = errorCode.getMsg();
    }

    public WxException(ErrorCode errorCode,String message){
        super(errorCode.getMsg());
        this.errCode = errorCode.getCode();
        if(message!=null && !message.isEmpty()){
            this.errMsg = message;
        }else{
            this.errMsg = errorCode.getMsg();
        }

    }

    public WxException(int errCode, String errMsg){
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WxException(Throwable cause){
        super(cause);
        if(cause!=null){
            this.errMsg = cause.getMessage();
        }
    }

    public WxException(int errCode, Throwable cause){
        super(cause);
        this.errCode = errCode;
        if(cause!=null){
            this.errMsg = cause.getMessage();
        }
    }

    public WxException(ErrorCode errorCode, Throwable cause){
        super(cause);
        this.errCode = errorCode.getCode();
        this.errMsg = errorCode.getMsg()+" "+cause.getMessage();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"errCode\"=\"");
        builder.append(errCode);
        builder.append("\", \"errMsg\"=\"");
        builder.append(errMsg);
        builder.append("\"}");
        return builder.toString();
    }
}
