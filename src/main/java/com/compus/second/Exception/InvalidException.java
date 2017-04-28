package com.compus.second.Exception;

import com.compus.second.Exception.Enum.INVALID_EXCEPTION_TYPE;

/**
 * Created by cai on 2017/3/17.
 */
public class InvalidException extends BaseException {

    public InvalidException(INVALID_EXCEPTION_TYPE e) {
        super(e);
        super.code = e.getCode();
        super.msg = e.getMsg();
    }

    public InvalidException(int code, String msg) {
        super(code, msg);
    }
}
