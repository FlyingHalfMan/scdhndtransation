package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cai on 2017/3/17.
 */

// 商品表
@Table(name = "cs_commodity")
@Component
@Entity
public class Commodity implements Serializable {

    private int     id;
    private String  commodityId;    // 商品的id
    private String  title;          // 商品的标题
    private int     sortId;         // 商品分类的id  用户上传的时候手动添加商品分类
    private String  sortName;       // 商品的分类名
    private int     count;         // 商品的数量
    private Float   price;          // 商品的价格
    private Date    publishDate;    // 发布的时间
    private String  userId;         // 发布的人
    private String  detail;       // 商品的简单描述
    private int     status;         // 商品的状态 1。正常出售 1  2。已经售出 2

    private int     delivery;       // 支持的配送方式
    private int     payment;        // 支持的支付方式

    private long    views;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
