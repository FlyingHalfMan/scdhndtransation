package com.compus.second.Table;

import antlr.StringUtils;
import com.compus.second.Bean.OrderBean;
import com.compus.second.Constant;
import com.compus.second.Utils.EncryptUtil;
import com.compus.second.Utils.Utils;
import com.sun.tools.internal.jxc.ap.Const;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cai on 2017/3/23.
 */
@Table(name = "cs_order")
@Entity
@Component
public class Order implements Serializable {

    private int id;
    private String  commodityId;    // 商品号
    private String  orderId;        // 订单号
    private String  orderDesc;      // 订单描述（使用商品的标题）
    private int     numbers;        // 商品购买数量
    private float   price;          // 价格
    private Date    orderDate;      // 下单时间
    private int     payment;        // 支付方式   1.在线付款  2。线下交易
    private int     delivery;       // 配送方式   1.上门提货，2。送货上门
    private String  address;        // 支付地址
    private int     status;         // 订单的当前状况  1。未付款 2。已经付款 3。交易完成 4。取消订单

    private String  buyerId;
    private String  mobile;
    private String  email;
    private String  name;


    public Order(){};

    public Order(String commodityId,String title,int count,float price,String userId,int payment, int delivery,String address,String contact,String name){

        this.commodityId    = commodityId;
        this.orderId        = EncryptUtil.randomString();
        this.orderDesc      = title;
        this.numbers        = count;
        this.price          = price;
        this.orderDate      = new Date();
        this.payment        =  payment;
        this.delivery       = delivery;
        this.address        = address;
        this.status         = Constant.ORDER_STATUS_WAIT_TO_PAY;
        this.buyerId        = userId;
        this.name           = name;
       if (Utils.isRightEmail(contact)) this.email = contact;
       else if(Utils.isRightMobile(contact)) this.mobile = contact;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(unique = true,nullable = false)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
