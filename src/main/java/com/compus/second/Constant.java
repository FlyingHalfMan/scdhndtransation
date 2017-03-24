package com.compus.second;

/**
 * Created by cai on 2017/3/24.
 */
public class Constant {


    /**
     * 商品的状态
     */
    public static final int COMMODITY_STATUS_ON_SALE  = 0;      // 正常出售
    public static final int COMMODITY_STATUS_OFF_SALE = 1;      // 下架
    public static final int COMMODITY_STATUS_ORDERED  = 2;      // 已经被下定
    public static final int COMMODITY_STATUS_SOLD     = 3;      // 已经出售


    /**
     * 商品的支付方式
     */
    public static final int COMMODITY_PAYMENT_ONLINE       = 0;  // 线上交易
    public static final int COMMODITY_PAYMENT_FACE_TO_FACE = 1;  // 线下交易
    public static final int COMODITY_PAYMENT_BOTH          = 2;  // 两种方式都支持


    /**
     * 配送方式
     */
    public static final int COMMODITY_DELIVERY_SELF      = 0;  // 上门自取
    public static final int COMMODITY_DELIVERY_SALER     = 1;  // 卖家配送
    public static final int COMODITY_DELIVERY_BOTH       = 2;  // 两种方式都支持

    /**
     *  订单状态
     */
    public static final int ORDER_STATUS_WAIT_TO_PAY    = 0;    // 等待付款
    public static final int ORDER_STATUS_CANCELED       = 1;    // 用户取消订单
    public static final int ORDER_STATUS_PAYED          = 2;    // 已经支付完成

}
