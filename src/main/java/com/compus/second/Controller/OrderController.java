package com.compus.second.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by cai on 2017/3/24.
 */

@Controller
public class OrderController extends BaseController {

    @RequestMapping(path = "orders")
    public ModelAndView gotOrderPage(){
        return new ModelAndView("orders",null);
    }

}
