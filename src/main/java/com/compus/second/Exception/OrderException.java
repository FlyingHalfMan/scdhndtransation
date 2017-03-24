package com.compus.second.Exception;

/**
 * Created by cai on 2017/3/24.
 */
public class OrderException extends BaseException {

    public OrderException(Enum e) {
        super(e);
    }

    public OrderException(int code, String msg) {
        super(code, msg);
    }
}
