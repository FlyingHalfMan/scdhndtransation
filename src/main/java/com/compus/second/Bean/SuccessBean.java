package com.compus.second.Bean;

import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
public class SuccessBean {
    private int code;
    private String msg;
    private Object model;
    private List<Object> Models;

    public SuccessBean(){};
    public SuccessBean(int code,String msg){this.code =code;this.msg = msg;}


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public List<Object> getModels() {
        return Models;
    }

    public void setModels(List<Object> models) {
        Models = models;
    }
}
