package com.compus.second.Exception;

/**
 * Created by cai on 2017/3/24.
 */
public class CartException extends BaseException {
    public CartException(Enum e) {
        super(e);
    }

    public CartException(int code, String msg) {
        super(code, msg);
    }
}
