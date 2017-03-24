package com.compus.second.Bean;

import java.io.Serializable;

/**
 * Created by cai on 2017/3/17.
 */
public class LoginBean  implements Serializable{

    private String count;
    private String pwd;
    private boolean keplogin;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isKeplogin() {
        return keplogin;
    }

    public void setKeplogin(boolean keplogin) {
        this.keplogin = keplogin;
    }
}
