package com.compus.second.Exception.Enum;

/**
 * Created by cai on 2017/3/24.
 */
public enum ORDER_EXCEPTION_TYPE {
    ORDER_EXCEPTION_COMMODITY_NOTENOUGH(-7001,"商品数量不足，无法进行购买"),
    ORDER_EXCEPTION_COMMODITY_NOT_FOUND(-7002,"未找到商品，或者商品已经下架"),
    ORDER_EXCEPTION_COMMODITY_OFF_SALE(-7003,"商品已经下架"),
    ORDER_EXCEPTION_COMMODITY_SALD(-7004,"商品已经售出"),
    ORDER_EXCEPTION_INVALIDE_PAYMENT(-7005,"卖家不支持该支付方式"),
    ORDER_EXCEPTION_INVALIDE_DELIVERY(-7006,"卖家不支持该配送方式"),
    ORDER_EXCEPTION_INVALIDE_COMMODITY_NUMBER(-7007,"商品数量不足"),
    ORDER_EXCEPTION_NOE_ORDERS(-7008,"还没有订单"),
    ORDER_EXCEPTION_DELETE_FAILED(-7009,"无法删除该类型的订单"),
    ORDER_EXCEPTION_UNAUTHERISIZED(-7010,"您无权进行此操作");


    private int code;
    private String msg;

    ORDER_EXCEPTION_TYPE(int code, String msg) {
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
