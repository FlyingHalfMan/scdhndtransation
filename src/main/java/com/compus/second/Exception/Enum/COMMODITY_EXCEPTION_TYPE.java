package com.compus.second.Exception.Enum;

/**
 * Created by cai on 2017/3/17.
 */
public enum COMMODITY_EXCEPTION_TYPE {

    COMMODITY_EXCEPTION_TYPE_NOTFOUND(-2001,"没有找到该商品信息"),
    COMMODITY_EXCEPTION_TYPE_ADDFAILED(-2002,"商品添加失败"),
    COMMODITY_EXCEPTION_TYPE_DELETEFAILED(-2003,"无法删除该商品，商品不存在"),
    COMMODITY_EXCEPTION_TYPE_UNAUTHENTICATED(-2004,"用户权限不足，删除失败"),
    COMMODITY_EXCEPTION_TYPE_UNABLETOADDIMAGE(-2005,"最多只能添加三张照片，无法继续添加");


    private int code;
    private String msg;


    COMMODITY_EXCEPTION_TYPE(int code, String msg) {
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
