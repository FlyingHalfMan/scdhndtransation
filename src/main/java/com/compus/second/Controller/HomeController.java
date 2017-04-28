package com.compus.second.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Created by cai on 2017/3/15.
 */
@Controller
public class HomeController extends BaseController {


    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        /**
         * 本网站全部要求登录后使用，为了防止用户通过使用直接输入网址的方式访问某些特殊网页，使用拦截器AuthenticateIntecaptor 对所有请求拦截
         * 客服端登录时服务端会自动读取请求中的cookie 获取token 来检验用户账户的真实性。
         * token 是一个由用户ID securityToken（密码加密后）和时间戳组成的字符串
         *
         */

        /**
         * 1. 判断是否存在cookie
         */
        return new ModelAndView("URL",null);
    }
}
