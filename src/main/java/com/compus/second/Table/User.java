package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by cai on 2017/3/16.
 */
@Component
@Table(name = "cs_user")
@Entity
public class User implements Serializable{

    private int id;                 // id 数据库组建，自增长类型
    private String userId;          // 用户id 鉴别用户身份的标示，唯一，由服务器自动生成，不可修改
    private String salt;            // 盐，由于对用户的密码进行加密
    private String token;           // 密码由盐加密后的字符串。
    private int auth;            // 用户的权限（管理员／普通用户，管理员以0 标示，其他用户从1开始标示，数值越大，权限越高，但是不高于管理员）
    private String password;

    // 其他的一些基本信息
    private String name;            // 用户名
    private String image;           // 用户头像
    private String mobile;          // 用户手,唯一，注册时候可以选择使用手机号码或者邮箱注册。
    private String email;           // 用户的邮箱，一个邮箱可以绑定多个账号。
    private String gender;          // 用户性别
    private String origion;         // 用户的籍贯
    private String birthday;        // 出生日期
    private String registDate;      // 注册日期


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(unique = true,nullable = false)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(unique = true,nullable = false)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(nullable = false)
    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOrigion() {
        return origion;
    }

    public void setOrigion(String origion) {
        this.origion = origion;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
