package com.compus.second.Bean;

import com.compus.second.Exception.BaseException;

import java.io.Serializable;

/**
 * Created by cai on 2017/3/17.
 */
public class ErrorBean  implements Serializable{

    private int code;
    private String msg;

    public ErrorBean(){};

    public ErrorBean(BaseException e) {
        this.code = e.getCode();
        this.msg = e.getMsg();
    }
    public ErrorBean(Exception e){
        this.code = 500;
        this.msg = e.getLocalizedMessage();
    }

    public ErrorBean(int code, String msg)
    {
      this.code = code;
      this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
