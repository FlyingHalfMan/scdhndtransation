package com.compus.second;

import com.compus.second.Utils.EncryptUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;

/**
 * Created by cai on 2017/3/15.
 */
public class AutenticateInterceptor implements HandlerInterceptor {



    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        /**
         * AuthenticateInterceptor  拦截器
         * 拦截器拦截所有的请求，对用户的进行身份验证和授权验证，通过比对用户信息表(User)和权限表(Authority)来进行验证授权
         */
        Cookie[]  cookies = httpServletRequest.getCookies();

        /**
         * cookie 不存在，跳转到登录页面
         */

        HttpSession session = httpServletRequest.getSession();
        String token = "";
        // 获取token,如果token 不存在就说明还没有登录，跳转到登录页面
        // token 格式 userId +角色Id +securityToken
        token = (String) session.getAttribute("token");
        String url = httpServletRequest.getRequestURI();
        if(token ==null || token.length() <1) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() +"/login");
//            Cookie[] cookies = httpServletRequest.getCookies();
            // 这个地方因该在cookie 中放置一个RSA公钥，用户客户端登录加密。上来后登录的时候再解密，使用salt进行解密验证身份，然后将token 放置到session中
//            for(Cookie cookie :cookies)
//            {
//                //
//                // 在cookie 中放置一个salt 用来进行用户加密
//                cookie.setValue(EncryptUtil.salt());
//            }

            return false;
        }
        String userId = token.substring(0,15);
        String role = token.substring(16,17);
        String securityToken = token.substring(17,token.length());
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
