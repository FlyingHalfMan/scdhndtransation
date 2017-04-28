package com.compus.second.Bean;

import com.compus.second.Table.User;

import java.io.Serializable;

/**
 * Created by cai on 2017/4/27.
 */
public class UserBean implements Serializable {

    private int    id;
    private String userId;
    private String userName;
    private String userGender;
    private String mobile;
    private String email;
    private String registDate;
    private String role;


    public UserBean(User user,int id) {
        this.id         = id;
        this.userId     = user.getUserId();
        this.userName   = user.getName()        ==null ? "未设置" : user.getName();
        this.userGender = user.getGender()      ==null ? "保密"   : user.getGender();
        this.mobile     = user.getMobile()      ==null ? "未设置" : user.getMobile();
        this.email      = user.getEmail()       ==null ? "未设置" : user.getEmail();
        this.registDate = user.getRegistDate();
        this.role       = user.getAuth()           > 1 ? "管理员" : "注册会员";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
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

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
