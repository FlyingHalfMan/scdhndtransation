package com.compus.second.Interceptor;

import com.compus.second.Dao.UserDao;
import com.compus.second.Utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;

/**
 * Created by cai on 2017/3/15.
 */
public class AutenticateInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        /**
         * AuthenticateInterceptor  拦截器
         * 拦截器拦截所有的请求，对用户的进行身份验证和授权验证，通过比对用户信息表(User)和权限表(Authority)来进行验证授权
         */
        Cookie[]  cookies = httpServletRequest.getCookies();

        HttpSession session = httpServletRequest.getSession();
        String userId = (String) session.getAttribute("userId");

        String token = (String) session.getAttribute("token");

        if (userId == null || userId.length() < 1 || token == null || token.length() < 1){
//            httpServletRequest.getRequestDispatcher("/login").forward(httpServletRequest,httpServletResponse);
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() +"/login");
            return false;
        }

        String url = httpServletRequest.getRequestURI();
        // 获取请求的url,验证权限

//        if(role)
//
//        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
