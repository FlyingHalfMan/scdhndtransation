package com.compus.second.Exception;

/**
 * Created by cai on 2017/3/18.
 */
public class InternalServerErrorException extends BaseException {


    public InternalServerErrorException(Enum e) {
        super(e);
    }

    public InternalServerErrorException(int code, String msg) {
        super(code, msg);
    }
}
