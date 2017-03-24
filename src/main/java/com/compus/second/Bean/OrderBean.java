package com.compus.second.Bean;

import com.compus.second.Table.Commodity;
import com.compus.second.Table.Order;

/**
 * Created by cai on 2017/3/24.
 */
public class OrderBean {

    private int id;                 // 序号
    private String commodityId;     // 商品的编号
    private String commodityImage;  // 商品的图片 1张
    private int    number;          // 购买商品的数量
    private float  price;           // 商品单价
    private float  totalPrice;      // 总价
    private String commodityDesc;   // 商品的描述
    private String orderDate;       // 订单生成日期
    private String orderStatus;     // 订单状态
    private String orderId;         // 订单号



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
