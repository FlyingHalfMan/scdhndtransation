package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by cai on 2017/3/17.
 */

/*
 *  verifycode 考虑 可以放到session当中，并设置session的有效时间
 */
@Table(name = "cs_verifycode")
@Entity
@Component
public class VerifyCode {

    private int id;
    private String userId;
    private String verifycode;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }
}
