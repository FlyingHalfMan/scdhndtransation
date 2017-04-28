package com.compus.second.Bean;

import com.compus.second.Constant;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.User;
import com.compus.second.Utils.Utils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
public class CommodityBean implements Serializable {

    private String commodityId;
    private String title;       // 商品的标题
    private String describe;    // 商品的描述
    private int    sortId;
    private String sortName;    // 分类的名称
    private float  price;       // 价格
    private List<String> images;
    private String status;      // 商品的状态
    private String userId;      // 用户id
    private String publishDate; // 发布的日期
    private String userName;    // 用户名
    private int    numbers;

    public CommodityBean(){};

    public CommodityBean(Commodity commodity,List<String> images, User user){
        this.commodityId = commodity.getCommodityId();
        this.title = commodity.getTitle();
        this.describe = commodity.getDetail();
        this.sortName = commodity.getSortName();
        this.price = commodity.getPrice();
        this.images = images;
        this.status = Constant.getCommodityStatusById(commodity.getStatus());
        this.publishDate = Utils.parseDateToString(commodity.getPublishDate(), Constant.dateFormate);
        this.userId = user.getUserId();
        this.userName = user.getName();
        this.numbers =commodity.getCount();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
}
