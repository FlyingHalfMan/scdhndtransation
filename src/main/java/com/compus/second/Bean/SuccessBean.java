package com.compus.second.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
public class SuccessBean implements Serializable{
    private int code;
    private String msg;
    private Object model;
    private List models;

    public SuccessBean(){};
    public SuccessBean(int code,String msg){this.code =code;this.msg = msg;}
    public SuccessBean(int code,String msg,Object model,List models){
        this.code = code;
        this.msg = msg;
        this.model = model;
        this.models = models;
    }


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

    public List getModels() {
        return models;
    }

    public void setModels(List models) {
        this.models = models;
    }
}
