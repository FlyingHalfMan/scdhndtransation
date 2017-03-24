package com.compus.second.Exception;


import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;

public class UserException extends BaseException {

    public UserException(USER_EXCEPTOIN_TYPE type)
    {
        super(type);
        this.code =type.getCode();
        this.msg = type.getMsg();
    }

    public UserException(int code, String msg) {
        super(code, msg);
    }
}
