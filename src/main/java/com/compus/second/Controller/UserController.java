package com.compus.second.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.INVALID_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.InvalidException;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.User;
import com.compus.second.Utils.EncryptUtil;
import com.compus.second.Utils.ImageService;
import com.compus.second.Utils.SMSService;
import com.compus.second.Utils.Utils;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cai on 2017/3/18.
 */

@Controller
public class UserController extends  BaseController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "userprofile")
    public ModelAndView goToProfilePaage() {

        return new ModelAndView("profile",null);
    }


    /**
     * 获取用户的信息
     * @param request
     * @param response
     * @return
     * @throws UserException
     */
    @RequestMapping(path = "profile")
    public ModelAndView getToUserInormationView(HttpServletRequest  request,
                                                HttpServletResponse response)throws UserException{
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
       User user =  userDao.findById(userId);
       if (user == null)
           throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        Map<String,User> model = new HashMap<String, User>();
        model.put("user",user);
        return new ModelAndView("",model);
    }

    /**
     * 检查用户名是否有效
     * @param name
     * @param request
     * @param response
     * @return
     */
    // 检查用户更新的名称是否有效
    @RequestMapping(path = "verify/name")
    public ModelAndView verifyName(@RequestParam("name") final String name,
                                   HttpServletRequest request,
                                   HttpServletResponse response){

      boolean isRight = Utils.isRightName(name);
      if (!isRight == true) {
          throw new InvalidException(403,"无效的用户名");
      }
        List<User> users = userDao.findUserByUserName(name);
      if (users !=null && users.size() >=1)
          throw new InvalidException(403,"用户名已经被占用，请使用另一个用户名");
       SuccessBean successBean =  new SuccessBean(200,"用户名可以使用");
       Map resp = new HashMap();
       resp.put("resp",successBean);
      return  new ModelAndView("/profile",resp);

    }

    /**
     * 修改基本信息，除了头像，手机号码，邮箱号码。这三个比较特殊，头像更新后要回调，手机号码和邮箱号码需要进行验证
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "update")
    public ModelAndView updateUser(@RequestBody final User user,
                                   HttpServletRequest request,
                                   HttpServletResponse  response){

       String userId = (String) request.getSession().getAttribute("userId");
       User user1 = userDao.findById(userId);
       user1.setName(user.getName());
       user1.setOrigion(user.getOrigion());
       user1.setGender(user.getGender());
       user1.setBirthday(user.getBirthday());
       userDao.update(user1);

       Map resp = new HashMap();
       resp.put("resp","信息修改成功");
       return new ModelAndView("profile",resp);
    }


    // 更新用户头像

    /**
     * 用户上传一张图片，图片会上传到图片服务器，然后将图片返回到浏览器浏览器向图片服务器请求裁剪后的图片
     * @param image
     * @param userId
     * @return
     * @throws IOException
     */
    @RequestMapping(path = "update/image")
    public ModelAndView updateImage(@RequestParam("iamge")CommonsMultipartFile image,
                                    @RequestHeader("userid") final String userId) throws IOException {
        User user = userDao.findById(userId);
        String imageName = ImageService.uploadImageToImageServer(image,userId);
        user.setImage(imageName);
        userDao.update(user);
        Map resp = new HashMap();
        resp.put("resp",imageName);
        return new ModelAndView("profile",resp);
    }

    /**
     * update mobile
     * @param mobile
     * @param request
     * @param response
     * @return
     * @throws ClientException
     */
    @RequestMapping("update/mobile/verify/{mobile}")
    public ModelAndView verifyMobile(@PathVariable("mobile") final String mobile,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws ClientException {

        if(!Utils.isRightMobile(mobile))
            throw new InvalidException(403,"无效的手机号");

        User user =userDao.findByCount(mobile);
        if (user !=null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_RIGHTED);

        String verifycode = EncryptUtil.getVerifyCode();
        HttpSession session = request.getSession();
        session.setAttribute("reset_mobile",mobile);
        session.setAttribute("reset_mobile_verifycode",verifycode);
        session.setAttribute("reset_mobile_verifycode_expired_date",Utils.getTimeWithDuration(5 *60 *1000));
        SMSService.sendMobileResetVerifyCode(mobile,verifycode);
        Map resp = new HashMap();
        resp.put("resp","验证码已经发送到手机"+mobile);
        return new ModelAndView("profile",resp);
    }

    /**
     *
     * @param mobile
     * @param verifycode
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */

    // 修改手机
    @RequestMapping(value = "update/mobile",method = RequestMethod.POST)
    public ModelAndView updateMobile(@RequestParam("mobile") final String mobile,
                                     @RequestParam("verifycode") final String verifycode,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws ParseException {
        String mobile_session = (String) request.getSession().getAttribute("reset_mobile");
        String verifycode_session = (String)request.getSession().getAttribute("reset_mobile_verifycode");
        String expiredDate = (String) request.getSession().getAttribute("reset_mobile_verifycode_expired_date");
        if(mobile_session == null || !mobile_session.equals(mobile)){
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);
        }
        if (!verifycode_session.equals(verifycode))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE);
        if(Utils.parseStringToDate(expiredDate,"yyyyMMddHHmmssSSS").before(new Date()))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_EXPIRED);

        User user = userDao.findById((Serializable) request.getSession().getAttribute("userid"));
        user.setMobile(mobile);
        userDao.update(user);

        request.getSession().removeAttribute("reset_mobile");
        request.getSession().removeAttribute("reset_mobile_verifycode");
        request.getSession().removeAttribute("reset_mobile_verifycode_expired_date");
        Map resp = new HashMap();
        resp.put("resp","手机号修改成功");
        return new ModelAndView("profile",resp);
    }


    /**
     *
     * @param email
     * @param request
     * @param response
     * @return
     */
    // 获取邮箱验证码
    @RequestMapping("update/email/verify/{email}")
    public ModelAndView verifyEmail(@PathVariable("email") final String email,
                                    HttpServletRequest request,
                                    HttpServletResponse response){

        if(!Utils.isRightEmail(email))
            throw new InvalidException(403,"无效的邮箱");

        User user =userDao.findByCount(email);
        if (user !=null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_RIGHTED);

        String verifycode = EncryptUtil.getVerifyCode();
        HttpSession session = request.getSession();
        session.setAttribute("reset_email",email);
        session.setAttribute("reset_email_verifycode",verifycode);
        session.setAttribute("reset_email_verifycode_expired_date",Utils.getTimeWithDuration(5 *60 *1000));
        SMSService.sendEmailVerifyCode(email,"校园二手市场",verifycode);
        Map resp = new HashMap();
        resp.put("resp","验证码已经发送到邮箱" +email);
        return new ModelAndView("profile",resp);
    }

    /**
     *
     * @param email
     * @param verifycode
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "upodate/email",method = RequestMethod.POST)
    public ModelAndView updateEmail(@RequestParam("email") final String email,
                                    @RequestParam("verifycode") final String verifycode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws ParseException {

            String email_session = (String) request.getSession().getAttribute("reset_email");
            String verifycode_session = (String)request.getSession().getAttribute("reset_email_verifycode");
            String expiredDate = (String) request.getSession().getAttribute("reset_email_verifycode_expired_date");


            if(email_session == null || !email_session.equals(email)){
                throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);
            }

            if (!verifycode_session.equals(verifycode))
                throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE);

            if(Utils.parseStringToDate(expiredDate,"yyyyMMddHHmmssSSS").before(new Date()))
                throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_EXPIRED);

            User user = userDao.findById((Serializable) request.getSession().getAttribute("userid"));
            user.setEmail(email);
            userDao.update(user);

            request.getSession().removeAttribute("reset_email");
            request.getSession().removeAttribute("reset_email_verifycode");
            request.getSession().removeAttribute("reset_email_verifycode_expired_date");
            Map resp = new HashMap();
            resp.put("resp","手机号修改成功");

            return new ModelAndView("profile",resp);
    }



    // 查看我购买的商品




    // 查看我最近查看的商品



    // 查看我的收藏


    // 查看我出售的商品





}
