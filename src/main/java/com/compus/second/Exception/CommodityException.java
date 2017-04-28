package com.compus.second.Exception;

import com.compus.second.Exception.Enum.COMMODITY_EXCEPTION_TYPE;
import com.compus.second.Table.Commodity;

/**
 * Created by cai on 2017/3/17.
 */
public class CommodityException extends BaseException {


    public CommodityException(COMMODITY_EXCEPTION_TYPE type) {
        super(type);
        super.code =type.getCode();
        super.msg = type.getMsg();
    }

    public CommodityException(int code, String msg) {
        super(code, msg);
    }
}
