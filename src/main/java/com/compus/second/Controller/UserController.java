package com.compus.second.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.compus.second.Bean.CommodityBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Constant;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.CommodityImageDao;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.INVALID_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.InvalidException;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.Commodity;
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
import java.util.*;

/**
 * Created by cai on 2017/3/18.
 */

@Controller
@RequestMapping("user")
public class UserController extends  BaseController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommodityDao commodityDao;
    @Autowired
    private CommodityImageDao commodityImageDao;

    @RequestMapping(path = "index")
    public ModelAndView goToProfilePage() {

        System.out.println("user/index");

        return new ModelAndView("user/index",null);
    }

    @RequestMapping("orders")
    public String orders(){
        return "user/orders";
    }

    @RequestMapping("commodities")
    public String commodities(){
        return "user/commodities";
    }

    @RequestMapping("sell")
    public String sell(){
        return "user/sell";
    }
    @RequestMapping("cart")
    public String cart(){
        return "user/cart";
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request){

        request.getSession().removeAttribute("userId");
        request.getSession().removeAttribute("token");
        request.getSession().removeAttribute("role");
        return "redirect:login";
    }



    /**
     * 获取用户的信息
     * @param request
     * @param response
     * @return
     * @throws UserException
     */
    @RequestMapping("profile")
    @ResponseBody
    public SuccessBean getUserInormationView(HttpServletRequest  request,
                                                HttpServletResponse response)throws UserException{
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user =  userDao.findById(userId);
       if (user == null)
           throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        return new SuccessBean(200,"信息获取成功",user,null);
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
    public SuccessBean verifyName(@RequestParam("name") final String name,
                                   HttpServletRequest request,
                                   HttpServletResponse response){

      boolean isRight = Utils.isRightName(name);
      if (!isRight == true) {
          throw new InvalidException(403,"无效的用户名");
      }
        List<User> users = userDao.findUserByUserName(name);
      if (users !=null && users.size() >=1)
          throw new InvalidException(403,"用户名已经被占用，请使用另一个用户名");

      return new SuccessBean(200,"用户名可以使用");

    }

    /**
     * 修改基本信息，除了头像，手机号码，邮箱号码。这三个比较特殊，头像更新后要回调，手机号码和邮箱号码需要进行验证
     * @param userName
     * @param origion
     * @param gender
     * @param birthdate
     * @param address
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "update")
    @ResponseBody
    public SuccessBean updateUser(@RequestParam("userName") final String userName,
                                  @RequestParam("origion") final String origion,
                                  @RequestParam("gender") final String gender,
                                  @RequestParam("birthdate") final String birthdate,
                                  @RequestParam("address") final String address,
                                  HttpServletRequest request,
                                  HttpServletResponse  response){

       String userId = (String) request.getSession().getAttribute("userId");
       User user1 = userDao.findById(userId);
       user1.setName(userName);
       user1.setOrigion(origion);
       user1.setGender(gender);
       user1.setBirthday(birthdate);
       userDao.update(user1);

       return new SuccessBean(200,"信息修改成功",user1,null);
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
    @ResponseBody
    public SuccessBean updateImage(@RequestParam("image")CommonsMultipartFile image,
                                    @SessionAttribute("userId") final String userId) throws IOException {
        User user = userDao.findById(userId);
        String imageName = ImageService.uploadImageToImageServer(image,userId);
        user.setImage(imageName);
        userDao.update(user);
        return new SuccessBean(200,"头像更新完成",user,null);
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
    @ResponseBody
    public SuccessBean verifyMobile(@PathVariable("mobile") final String mobile,
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
        SMSService.sendResetPwdVerifyCode(mobile,verifycode);

        return new SuccessBean(200,"验证码已经发送到手机"+mobile);
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
    @ResponseBody
    public SuccessBean updateMobile(@RequestParam("mobile") final String mobile,
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
        return new SuccessBean(200,"手机号码修改成功",user,null);
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
    @ResponseBody
    public SuccessBean verifyEmail(@PathVariable("email") final String email,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws ClientException {

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

        return new SuccessBean(200,"验证码已经发送到邮箱" +email);
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
    @RequestMapping(value = "update/email",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean updateEmail(@RequestParam("email")      final String email,
                                   @RequestParam("verifycode") final String verifycode,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws ParseException {

        String email_session = (String) request.getSession().getAttribute("reset_email");
        String verifycode_session = (String)request.getSession().getAttribute("reset_email_verifycode");
        String expiredDate = (String) request.getSession().getAttribute("reset_email_verifycode_expired_date");


        if(email_session == null || !email_session.equals(email))
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_VERIFYCODE_NOT_SEND);

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

        return new SuccessBean(200,"邮箱修改成功",user,null);
    }

    // 查看我出售的商品

    /**
     *
     * @param status
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("sold")
    @ResponseBody
    public SuccessBean commodities(@RequestParam("order") final int status,
                                   @RequestParam("offset") final int offset,
                                   @RequestParam("limit") final int limit,
                                   HttpServletRequest request,
                                   HttpServletResponse response){

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userDao.findById(userId);

        List<Commodity> commodityList  = commodityDao.listCommodityByUserId(userId,status,offset,limit);
        if (commodityList == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_NONE_COMMODITY);

        List<CommodityBean> commodityBeans = new ArrayList<CommodityBean>();
        for (Commodity commodity: commodityList){
            List<String> images=  commodityImageDao.findByCommodity(commodity.getCommodityId());
            CommodityBean commodityBean = new CommodityBean(commodity,images,user);
            commodityBeans.add(commodityBean);
        }

        return new SuccessBean(200,"数据查找成功",null,commodityBeans);
    }
}
