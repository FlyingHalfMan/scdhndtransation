package com.compus.second.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cai on 2017/3/15.
 */
@Controller
public class HomeController extends BaseController {


    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
    {
        return new ModelAndView("index",null);
    }
}
