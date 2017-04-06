package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by cai on 2017/3/17.
 */

// 商品图片的中间表

@Table(name = "cs_commodityImage")
@Entity
@Component
public class CommodityImage {

    private int id;             //数据库存储id
    private String commodityId;   // 商品的id
    private String imageName;   // 图片的名称，图片在上传的时候会进行重新的命名，命名方式未商品id+时间戳

    public CommodityImage(){};
    public CommodityImage(String commodityId,String imageName){this.commodityId =commodityId;this.imageName =imageName;};

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

    @Column(unique = true)
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
