package com.compus.second.Controller;

import com.compus.second.Bean.ErrorBean;
import com.compus.second.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by cai on 2017/3/15.
 */
@Controller
public class BaseController {

    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> userException(UserException e) {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(OrderException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> orderException(OrderException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(CartException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> cartException(CartException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CommodityException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> commodityException(CommodityException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> internalServerErrorException(InternalServerErrorException e)
    {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseBody
    public ResponseEntity<ErrorBean> invalidException(InvalidException e) {
        ErrorBean error  = new ErrorBean(e);
        return new ResponseEntity<ErrorBean>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
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
