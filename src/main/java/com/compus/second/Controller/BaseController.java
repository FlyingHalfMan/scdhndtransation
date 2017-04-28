package com.compus.second.Controller;

import com.compus.second.Bean.ErrorBean;
import com.compus.second.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by cai on 2017/3/15.
 */
@Controller
public class BaseController {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorBean> userException(UserException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorBean> orderException(OrderException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorBean> cartException(CartException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CommodityException.class)
    public ResponseEntity<ErrorBean> commodityException(CommodityException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorBean> internalServerErrorException(InternalServerErrorException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ErrorBean> invalidException(InvalidException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorBean> ioException(IOException e){

        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    protected Map<String,Object> responsBody(Object object){

        Map<String,Object> resp = new HashMap<String,Object>();
        resp.put("resp",object);
        return resp;
    }
}
