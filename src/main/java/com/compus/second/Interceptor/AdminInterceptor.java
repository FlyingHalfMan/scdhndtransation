package com.compus.second.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cai on 2017/4/4.
 */
public class AdminInterceptor extends AutenticateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        super.preHandle(httpServletRequest, httpServletResponse, o);

//        管理员的权限管理
        /*
        *  查看全部的商品，订单，用户
        * */
        return true;
    }
}
