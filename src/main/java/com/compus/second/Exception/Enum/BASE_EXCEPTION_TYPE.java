package com.compus.second.Exception.Enum;

/**
 * Created by cai on 2017/3/17.
 */
public enum BASE_EXCEPTION_TYPE {
;
    public  int code;
    public  String msg;

    BASE_EXCEPTION_TYPE(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
