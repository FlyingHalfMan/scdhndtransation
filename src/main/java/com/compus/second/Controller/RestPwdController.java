package com.compus.second.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.compus.second.Bean.PasswordBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Bean.VerifycodeBean;
import com.compus.second.Constant;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.INVALID_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.InvalidException;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.User;
import com.compus.second.Utils.EncryptUtil;
import com.compus.second.Utils.SMSService;
import com.compus.second.Utils.Utils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by cai on 2017/4/5.
 */

@Controller
public class RestPwdController {

    @Autowired
    private UserDao userDao;

    /**
     * 跳转到密码重置页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "resetpwd")
    public String gotoResetPage(HttpServletRequest request,
                                HttpServletResponse response){

        return "resetpwd";
    }


    /**
     * 发送密码重置验证码
     * @param request
     * @param response
     * @param count
     * @return
     * @throws ParseException
     * @throws ClientException
     */
    @RequestMapping(path = "resetpwd/verifycode/{count}")
    public SuccessBean getResetVerifyCode(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable("count") final String count) throws ParseException, ClientException {

        if (!Utils.isRightMobile(count) && !Utils.isRightEmail(count))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_COUNT);

        User user = userDao.findByCount(count);
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);

        String verifycode = EncryptUtil.getVerifyCode();
        if (Utils.isRightMobile(count)) {
            //发送手机验证码
            SMSService.sendResetPwdVerifyCode(count, verifycode);
        } else if (Utils.isRightEmail(count)) {
            //发送邮件验证码
            SMSService.sendEmailVerifyCode(count, "校园二手", "注册码是：" + verifycode);
        } else {
            // 抛出无效账号
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_INVALIDCOUNT);
        }

        // 将请求注册的手机和验证码，验证码过期时间写入到session中
        HttpSession session = request.getSession();
        // 将count 写入到 session 中 表明当前注册的账号
        session.setAttribute(Constant.SEESION_RESET_COUNT, count);
        // 将验证码入到session中
        session.setAttribute(Constant.SESSION_RESET_VERIFYCODE, verifycode);
        // 设置验证码的过期时间
        session.setAttribute(Constant.SESSION_RESET_EXPIREDDATE, Utils.getTimeWithDuration(5 * 60 * 1000));
        session.setAttribute(Constant.SESSION_SALT, EncryptUtil.salt());
        session.setMaxInactiveInterval(1 * 60 * 1000);
        return new SuccessBean(200, "验证码已经发送到" + count + "请注意查收", null, null);
    }


    /**
     *  验证验证码的有效性
     * @param verifycodeBean
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(path = "reset/verify",method = RequestMethod.POST)
    public SuccessBean verify(@RequestBody VerifycodeBean verifycodeBean,
                              HttpServletRequest  request,
                              HttpServletResponse response,
                              @SessionAttribute(Constant.SEESION_RESET_COUNT) final String reset_count,
                              @SessionAttribute(Constant.SESSION_RESET_EXPIREDDATE) final Date date,
                              @SessionAttribute(Constant.SESSION_RESET_VERIFYCODE) final String verifycode){

        // 没有发送过验证码
        if (reset_count == null || reset_count !=verifycodeBean.getCount())
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);
        // 无效验证码
        if (verifycode == null || !verifycode.equals(verifycodeBean.getVerifycode()))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE);
        //验证码失效
        if (date.before(new Date()))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_EXPIRED);

        User user = userDao.findByCount(verifycodeBean.getCount());
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);

        Cookie countCookie = new Cookie("count",verifycodeBean.getCount());
        countCookie.setMaxAge(1 *60 *60 *1000);
        response.addCookie(countCookie);

        Cookie saltCookie = new Cookie("salt",user.getSalt());
        countCookie.setMaxAge(1 *60 *60 *1000);
        response.addCookie(saltCookie);

        return new SuccessBean(200,"验证成功",null,null);
    }


    /**
     * 密码重置
     * @param passwordBean
     * @return
     */

    @RequestMapping(path = "resetpwd/pwd",method = RequestMethod.POST)
    public SuccessBean resetPwd(@RequestBody PasswordBean passwordBean,
                                @SessionAttribute(Constant.SESSION_REGIST_COUNT) final String count,
                                HttpServletRequest request,
                                HttpServletResponse response){

        if (count == null)
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);

        if (!passwordBean.getPassword().equals(passwordBean.getRepeat()))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_DIRRERENT_PASSWORD);

        User user = userDao.findByCount(count);
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        user.setPassword(passwordBean.getPassword());
        userDao.update(user);
        return new SuccessBean(200,"密码重置成功",null,null);
    }
}
