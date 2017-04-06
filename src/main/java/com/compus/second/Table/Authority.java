package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by cai on 2017/3/20.
 */

// 保存各项权限对应的url
@Entity
@Table(name = "cs_authority")
@Component
public class Authority {

    private int id;
    private int auth;
    private String path;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
