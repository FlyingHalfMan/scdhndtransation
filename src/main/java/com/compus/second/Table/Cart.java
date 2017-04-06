package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cai on 2017/3/23.
 */
@Table(name = "cs_cart")
@Entity
@Component
public class Cart implements Serializable{

    private int     id;             // 购物车id
    private String  commodityId;    // 商品id
    private int     number;         // 商品数量
    private Date    addDate;        // 添加时间
    private String  userId;         // 添加的用户


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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
