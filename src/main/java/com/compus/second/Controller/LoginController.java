package com.compus.second.Controller;

import com.compus.second.Bean.ErrorBean;
import com.compus.second.Bean.LoginBean;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.User;
import com.compus.second.Utils.EncryptUtil;
import com.compus.second.Utils.ImageService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by cai on 2017/3/15.
 */

@Controller
public class LoginController extends BaseController {

    /**
     * 跳转到登录页面上
     *
     * @param request
     * @param response
     * @return
     */

    @Autowired
     private  UserDao userDao;


    /**
     * 跳转到登录页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("login", null);
    }


    /**
     *
     * @param request       http 请求
     * @param response      http 相应
     * @param loginBean
     * @return
     */

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ModelAndView loginWithPwd(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam("count")         final String count,
                                     @RequestParam("password")      final String password,
                                     @RequestParam(value = "keplogin",required = false) final boolean keplogin)  {

        /**
         *  获取登录信息中的账号和密码，
         *  抛出账号没有登录
         */
            User user = userDao.findByCount(count);
            String enPwd = EncryptUtil.encrypt(user.getSalt(), password);

        /**
         * 密码错误，返回到页面
         */
        if (!enPwd.equals(user.getToken()))
            {
                throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_WRONGPWD.getCode(),
                        USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_WRONGPWD.getMsg());
            }
            else
            {
                HttpSession session = request.getSession();
                session.setAttribute("userId",user.getUserId());        // 用户的id
                session.setAttribute("token",user.getToken());          // 用户的安全验证
                session.setAttribute("role",user.getAuth());            // 用户的角色
                return new ModelAndView("/index",null);
            }

    }

//    @RequestMapping("verifycode")
//    public void getVerifyCode(HttpServletRequest request,HttpServletResponse response) throws IOException
//    {
//        // 获取一个随机的验证图片
//        Object[] objects = ImageService.createImage();
//        HttpSession session = request.getSession();
//        session.setAttribute("code",objects[1]);
//        BufferedImage image = (BufferedImage) objects[1];
//        response.setContentType("image/gif");
//        OutputStream os = response.getOutputStream();
//        ImageIO.write(image, "gif", os);
//        os.close();
//
//    }



}
