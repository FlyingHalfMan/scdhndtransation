package com.compus.second.Exception;

/**
 * Created by cai on 2017/3/16.
 */
public class BaseException  extends RuntimeException{

    public int code;
    public String msg;

    public BaseException(Enum e)
    {

    }


    public BaseException(int code,String msg)
    {
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

    public void printException()
    {
        System.out.println("" +code + msg);
    }
}
