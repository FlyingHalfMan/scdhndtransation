package com.compus.second.Bean;

import java.util.List;

/**
 * Created by cai on 2017/4/4.
 */
public class CartBean {

    private int   cardId;
    private float price;        //  商品价格
    private int   numbers;      //  商品数量
    private List<CommodityBean> commodityBeans;


    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public List<CommodityBean> getCommodityBeans() {
        return commodityBeans;
    }

    public void setCommodityBeans(List<CommodityBean> commodityBeans) {
        this.commodityBeans = commodityBeans;
    }
}
