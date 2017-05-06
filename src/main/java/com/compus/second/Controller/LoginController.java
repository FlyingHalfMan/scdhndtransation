package com.compus.second.Controller;

import com.compus.second.Bean.LoginBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.User;
import com.compus.second.Utils.EncryptUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;

/**
 * Created by cai on 2017/3/15.
 */

@Controller
public class LoginController extends BaseController {

    @Autowired
     private  UserDao userDao;


    /**
     * 跳转到登录页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest  request,
                              HttpServletResponse response) {

        return new ModelAndView("login", null);
    }


    /**
     *
     * @param request       http 请求
     * @param response      http 相应
     * @return
     */

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean loginWithPwd(@RequestParam("count") final String count,
                                    @RequestParam("pwd") final String pwd,
                                    HttpServletRequest request,
                                    HttpServletResponse response
                                    )  {

        /**
         *  获取登录信息中的账号和密码，
         *  抛出账号没有登录
         */
            User user = userDao.findByCount(count);
            if (user == null)
                throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
            String enPwd = EncryptUtil.encrypt(user.getSalt(), pwd);

        /**
         * 密码错误，返回到页面
         */
        if (!enPwd.equals(user.getToken())) {
                throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_WRONGPWD.getCode(),
                        USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_WRONGPWD.getMsg());
            }
            else {
                String url = "http://localhost:8080/second/shop/index.html";
                HttpSession session = request.getSession();
                session.setAttribute("userId",user.getUserId());        // 用户的id
                session.setAttribute("token",user.getToken());          // 用户的安全验证
                session.setAttribute("role",user.getAuth());            // 用户的角色
                return new SuccessBean(200,"登录成功",url,null);
            }

    }
}
