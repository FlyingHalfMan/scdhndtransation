package com.compus.second.Bean;

import com.compus.second.Constant;
import com.compus.second.Table.Cart;
import com.compus.second.Table.Commodity;
import com.compus.second.Utils.Utils;

/**
 * Created by cai on 2017/4/4.
 */
public class CartCommodity {

    private String  commodityId;
    private String  title;
    private float   price;
    private int     numbers;
    private String  image;
    private String  addDate;


    public CartCommodity(Commodity commodity, Cart cart){
        this.commodityId = commodity.getCommodityId();
        this.title = commodity.getTitle();
        this.price = commodity.getPrice();
        this.numbers = cart.getNumber();
        this.addDate = Utils.parseDateToString(cart.getAddDate(), Constant.dateFormate);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}
