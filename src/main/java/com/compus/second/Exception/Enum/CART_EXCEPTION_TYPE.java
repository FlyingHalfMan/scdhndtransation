package com.compus.second.Exception.Enum;

/**
 * Created by cai on 2017/3/24.
 */
public enum  CART_EXCEPTION_TYPE {

    CART_EXCEPTION_COMMODITY_SOLD_OUT(-8001,"无法添加至购物车，商品已经卖完了"),
    CART_EXCEPTION_COMMODITY_OFF_SALE(-8002,"无法添加购物车，商品不存在或者已经下架");


    private int code;
    private String msg;

    CART_EXCEPTION_TYPE(int code, String msg) {
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
