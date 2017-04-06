package com.compus.second.Bean;

import com.compus.second.Constant;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.Order;
import com.compus.second.Table.User;
import com.compus.second.Utils.Utils;

/**
 * Created by cai on 2017/3/24.
 */
public class OrderBean {

    private String commodityId;     // 商品的编号
    private String commodityImage;  // 商品的图片 1张(客户端上传的时候可以不包含)
    private int    number;          // 购买商品的数量
    private float  price;           // 商品单价
    private float  totalPrice;      // 总价
    private String commodityDesc;   // 商品的描述
    private String orderDate;       // 订单生成日期
    private String orderStatus;     // 订单状态
    private String orderId;         // 订单号

    private int     paymentId;       // 客户端上传使用
    private String  payment;         // 服务端返回使用

    private int     deliveryId;       // 配送方式id
    private String  delivery;        // 配送方式
    private String  address;
    private String  buyername;
    private String  buyerEmail;
    private String  buyerMobile;
    private String  buyerId;


    public OrderBean() {
    };
    public OrderBean(Order order,Commodity commodity){
        this.commodityId = order.getCommodityId();
        this.commodityDesc = commodity.getDetail();
        this.number = order.getNumbers();
        this.totalPrice = order.getPrice();
        this.orderDate = Utils.parseDateToString(order.getOrderDate(), Constant.dateFormate);
        this.orderStatus = Constant.getOderStatusByStatusId(order.getStatus());
        this.orderId = order.getOrderId();
        this.payment = Constant.getPaymenntByPayId(order.getPayment());

        this.address = order.getAddress();
        this.buyerEmail = order.getEmail();
        this.buyerMobile = order.getMobile();
        this.buyerId = order.getBuyerId();

    }


    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityImage() {
        return commodityImage;
    }

    public void setCommodityImage(String commodityImage) {
        this.commodityImage = commodityImage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerMobile() {
        return buyerMobile;
    }

    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
