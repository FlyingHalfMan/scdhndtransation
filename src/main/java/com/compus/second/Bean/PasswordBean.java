package com.compus.second.Bean;

import java.io.Serializable;

/**
 * Created by cai on 2017/4/6.
 */
public class PasswordBean implements Serializable {

    private String password;
    private String repeat;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
