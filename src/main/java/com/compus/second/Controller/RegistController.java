package com.compus.second.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.compus.second.Bean.SuccessBean;
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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cai on 2017/3/15.
 */
@Controller
public class RegistController extends BaseController {

    @Autowired
    private UserDao userDao;

    /**
     * 跳转到注册页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "regist",method = RequestMethod.GET)
    public ModelAndView gotRegistPage(HttpServletRequest request,HttpServletResponse response)
    {
        return  new ModelAndView("regist",null);
    }

    /**
     *  注册请求
     * @param password  账号的密码，客户端使用cookie中的salt加密后发送到服务端，必须和repeat一致
     * @param repeat    再次输入的密码，必须与password一致
     * @param request
     * @param response
     * @return          注册成功后会将 userId securityToken auth 和验证过期时间组合成一个token 有效时间24小时写入到session中。
     */
    @RequestMapping(path = "regist",method = RequestMethod.POST)
    public ModelAndView regist(@RequestParam("password") final String password,
                               @RequestParam("repeat") final String repeat,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        String count = (String) request.getSession().getAttribute("count");

        //先验证账号的有效性，是否是正确的账号，账号是否已经注册。
        if(Utils.isRightEmail(count)) {
            //正确的邮箱
        }else if(Utils.isRightMobile(count)){
            //正确的手机
        }else{
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_INVALIDCOUNT);
        }

        // 判断账号是不是一致
        String sessionCount  = (String) request.getSession().getAttribute("count");
        if (count ==null ||count.length() < 1)
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);
        if(!count.equals(sessionCount))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_COUNT_NOT_REQUEST_REGIST);

        // 检查账号密码是否有效
        if (!password.equals(repeat))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_DIRRERENT_PASSWORD);

        // 创建新用户
        User user =new User();
        user.setUserId(EncryptUtil.getUUID());                                      // 获取用户id
        user.setSalt((String) request.getSession().getAttribute("salt"));       // 设置密码加盐

        //设置账号
        if(Utils.isRightEmail(count)) user.setEmail(count);
        else if(Utils.isRightMobile(count)) user.setMobile(count);


        user.setPassword(password);
        user.setAuth(1);
        user.setRegistDate(Utils.getCurrentTime());
        userDao.addUser(user);

        // 将token 写入到session
        HttpSession session = request.getSession();
        session.setAttribute("userId",user.getUserId());
        session.setAttribute("token",user.getToken());
        session.setAttribute("role",user.getAuth());
        // 设置session的过期时间
        session.setMaxInactiveInterval(24 * 60 *60 * 1000);
        session.removeAttribute("salt");
        return new ModelAndView("index",null);
    }

    /**
     *  获取注册验证码
     * @param count
     * @param request
     * @param response
     * @return
     * @throws ClientException
     */
    @RequestMapping(path = "regist/verifycode",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean getVerifyCode(@RequestParam("count") final  String count,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws ClientException {

        String verifycode = EncryptUtil.getVerifyCode();
        User user = userDao.findByCount(count);


        if (user !=null) throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_RIGHTED);
        // 判断是不是手机
        if(Utils.isRightMobile(count)) {
           //发送手机验证码
           SMSService.sendRegistVerifyCode(count,verifycode);
        }
        else if(Utils.isRightEmail(count)) {
           //发送邮件验证码
           SMSService.sendEmailVerifyCode(count,"校园二手",verifycode);
        }
        else {
           // 抛出无效账号
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_INVALIDCOUNT);
        }

        // 将请求注册的手机和验证码，验证码过期时间写入到session中
        HttpSession session = request.getSession();
        // 将count 写入到 session 中 表明当前注册的账号
        session.setAttribute(Constant.SESSION_REGIST_COUNT,count);
        // 将验证码入到session中
        session.setAttribute(count,verifycode);
        // 设置验证码的过期时间
        session.setAttribute(Constant.SESSION_REGIST_EXPIRED_DATE,Utils.getTimeWithDuration(5 *60 *1000));
        session.setAttribute(Constant.SESSION_SALT,EncryptUtil.salt());

        //return new ResponseEntity<SuccessBean>(new SuccessBean(200,"验证码已经成功发送到"+count), HttpStatus.ACCEPTED);
        return new SuccessBean(200,"验证码已经成功发送到"+count);
    }


    /**
     * 验证注册是否通过，并且跳转到下一个页面
     * @param count
     * @param verifycode
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(path = "regist/verify",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean verifyCount(@RequestParam("count") final String count,
                              @RequestParam("verifycode") final String verifycode,
                              HttpServletRequest request,
                              HttpServletResponse response) throws ParseException, IOException {
        // 验证账号的有效性
        if(!Utils.isRightMobile(count) && !Utils.isRightEmail(count))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_COUNT);

        String salt = (String) request.getSession().getAttribute("salt");

        String code = (String) request.getSession().getAttribute(count);
        if (code == null )
            //没有发送过验证码
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);
        else if(!code.equals(verifycode))
            // 验证码错误
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE);

        String dateStr = (String)request.getSession().getAttribute(Constant.SESSION_REGIST_EXPIRED_DATE);
        Date   date = Utils.parseStringToDate(dateStr,"yyyy-MM-dd HH:mm:ss.SSS");

        if(date.before(new Date())) {
            //验证码失效
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE);
        }

        //将salt 写入到cookie中
        Cookie countCookie = new Cookie(Constant.SESSION_REGIST_COUNT,count);
        countCookie.setMaxAge(30*60);
        response.addCookie(countCookie);
        Cookie cookie = new Cookie(Constant.SESSION_SALT,salt);
        cookie.setMaxAge(30 *60);
        response.addCookie(cookie);
        response.flushBuffer();
        return new SuccessBean(200,"http://localhost:8080/second/regist/password?s="+salt);
    }


    @RequestMapping("regist/password")
    public String goToSetPassword(HttpServletRequest request,
                                  HttpServletResponse response){
        HttpSession session = request.getSession();
        if (session.getAttribute(Constant.SESSION_REGIST_COUNT) == null){
            return "redirect:404.html";
        }

        return "password";
    }

    @RequestMapping(path = "regist/password",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean updatePassword(@RequestParam("password") final String password,
                                 @RequestParam("repeat") final String repeat,
                                 @SessionAttribute(Constant.SESSION_REGIST_COUNT) final String count,
                                 @SessionAttribute(Constant.SESSION_SALT) final String salt,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

        if (password.length() < 6)
            throw new InvalidException(403,"密码长度不足6位");
        if (!password.equals(repeat))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_DIRRERENT_PASSWORD);


        User user = new User();
        user.setSalt(salt);
        user.setRegistDate(Utils.parseDateToString(new Date(),Constant.dateFormate));
        user.setToken(EncryptUtil.encrypt(salt,password));
        user.setAuth(1);
        user.setEmail(count);
        user.setUserId(EncryptUtil.randomString(8));
        user.setGender("保密");
        user.setName("用户" + user.getUserId());
        userDao.addUser(user);
        HttpSession session =  request.getSession();
        session.setAttribute("userid",user.getUserId());
        session.setAttribute("count",user.getEmail());
        session.setAttribute("token",user.getToken());
        session.setAttribute("role",user.getAuth());
        session.setMaxInactiveInterval(6 * 60 * 60 * 1000);

        session.removeAttribute(Constant.SESSION_REGIST_COUNT);
        session.removeAttribute(Constant.SESSION_REGIST_VERIFYCODE);
        session.removeAttribute(Constant.SESSION_REGIST_EXPIRED_DATE);
        return new SuccessBean(200,"注册成功");
    }

}
