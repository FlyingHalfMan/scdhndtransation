package com.compus.second.Exception.Enum;

/**
 * Created by cai on 2017/3/16.
 */
public enum  USER_EXCEPTOIN_TYPE{

    USER_EXCEPTOIN_TYPE_NOTRIGST(-1001,"该账号还没有注册"),
    USER_EXCEPTOIN_TYPE_USER_NOT_FOUND(-1002,"用户不存在"),
    USER_EXCEPTOIN_TYPE_RIGHTED(-1003,"该账号已经注册，请使用其他手机号或者邮箱"),
    USER_EXCEPTOIN_TYPE_INVALIDCOUNT(-1004,"无效的账号"),
    USER_EXCEPTOIN_TYPE_WRONGPWD(-1005,"密码错误"),
    USER_EXCEPTOIN_TYPE_NONEUSER(-1006,"没有数据");


    private int code;
    private String msg;

    USER_EXCEPTOIN_TYPE(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
