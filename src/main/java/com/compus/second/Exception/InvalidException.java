package com.compus.second.Exception;

/**
 * Created by cai on 2017/3/17.
 */
public class InvalidException extends BaseException {

    public InvalidException(Enum e) {
        super(e);
    }

    public InvalidException(int code, String msg) {
        super(code, msg);
    }
}
