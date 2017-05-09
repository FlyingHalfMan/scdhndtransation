package com.compus.second;

/**
 * Created by cai on 2017/3/24.
 */
public class Constant {

    public static final String dateFormate = "yyyy-MM-dd HH:mm:ss";


    /**
     * 商品的状态
     */
    public static final int COMMODITY_STATUS_ON_SALE  = 0;      // 正常出售
    public static final int COMMODITY_STATUS_OFF_SALE = 1;      // 下架
    public static final int COMMODITY_STATUS_ORDERED  = 2;      // 已经被下定
    public static final int COMMODITY_STATUS_SOLD     = 3;      // 已经出售
    public static final int COMMODITY_STATUS_SOLD_OUT = 4;      // 卖光了
    public static final int COMMODITY_STATUS_WAIT_CHECK = 5;    // 等待商品审核中

    public static String getCommodityStatusById(int sortId){

        switch (sortId){
            case 1 : return "已经下架";
            case 2 : return "已经被下定";
            case 3 : return "已经售出";
            case 5 : return "正在等待审核";
            default: return "正常出售";
        }
    }


    /**
     * 商品的支付方式
     */
    public static final int COMMODITY_PAYMENT_ONLINE       = 0;  // 线上交易
    public static final int COMMODITY_PAYMENT_FACE_TO_FACE = 1;  // 线下交易
    public static final int COMODITY_PAYMENT_BOTH          = 2;  // 两种方式都支持

    public static String getPaymenntByPayId(int payId){

        switch (payId){
            case 1 : return "货到付款";
            default: return "在线支付";
        }
    }


    /**
     * 配送方式
     */
    public static final int COMMODITY_DELIVERY_SELF      = 0;  // 上门自取
    public static final int COMMODITY_DELIVERY_SALER     = 1;  // 卖家配送
    public static final int COMODITY_DELIVERY_BOTH       = 2;  // 两种方式都支持

    public static String getDeliveryByDeliveryId(int deliveryId){

        switch (deliveryId){
            case 1 : return "卖家配送";
            default: return "买家自取";
        }
    }

    /**
     *  订单状态
     */
    public static final int ORDER_STATUS_NORMAL         = 0;    // 正常出售
    public static final int ORDER_STATUS_WAIT_TO_PAY    = 1;    // 等待付款
    public static final int ORDER_STATUS_PAYED          = 2;    // 已经支付完成
    public static final int ORDER_STATUS_COMPLETE       = 3;    // 订单完成
    public static final int ORDER_STATUS_CANCELED       = 4;    // 订单取消

    public static String getOderStatusByStatusId(int id){

        switch (id){
            case 1: return "等待付款";
            case 2: return "订单支付完成";
            case 3: return "等待支付";
            case 4: return "订单已经取消";
            default:return "正常出售";
        }
    }


    public static final String SESSION_SALT                 = "salt";


    public static final String SESSION_REGIST_COUNT         = "count_regist";
    public static final String SESSION_REGIST_VERIFYCODE    = "verifycode_regist";
    public static final String SESSION_REGIST_EXPIRED_DATE  = "expiredDate_regist";



    public static final String SESSION_LOGIN_COUNT          = "count_login";
    public static final String SESSION_LOGIN_VERIFYCODE     = "verifycode_login";


    public static final String SEESION_RESET_COUNT          = "count_reset";
    public static final String SESSION_RESET_VERIFYCODE     = "verifycode_reset";
    public static final String SESSION_RESET_EXPIREDDATE    = "expiredDate_reset";



    // 商品的排序方式

    public static final int  COMMODITY_ORDER_BY_DATE_DESC  = 0; // 时间降序
    public static final int  COMMODITY_ORDER_BY_DATE_ASC   = 1; // 时间升序
    public static final int  COMMODITY_ORDER_BY_PRICE_DESC = 2; // 价格降序
    public static final int  COMMODITY_ORDER_BY_PRICE_ASC  = 3; // 时间升序


}
