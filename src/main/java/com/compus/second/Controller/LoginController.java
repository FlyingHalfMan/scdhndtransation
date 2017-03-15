package com.compus.second.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cai on 2017/3/15.
 */

@Controller
@RequestMapping(path = "/login")
public class LoginController extends BaseController {

    /**
     *  跳转到登录页面上
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response)
    {
        return  new ModelAndView("login",null);
    }

}
