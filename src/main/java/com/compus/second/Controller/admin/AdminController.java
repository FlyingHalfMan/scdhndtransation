package com.compus.second.Controller.admin;

import com.compus.second.Bean.CommodityBean;
import com.compus.second.Bean.OrderBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Bean.UserBean;
import com.compus.second.Constant;
import com.compus.second.Controller.BaseController;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.CommodityImageDao;
import com.compus.second.Dao.OrderDao;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.ORDER_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.OrderException;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.CommodityImage;
import com.compus.second.Table.Order;
import com.compus.second.Table.User;
import com.compus.second.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by cai on 2017/3/18.
 */
@RequestMapping(path = "/admin")
@Controller
public class AdminController extends BaseController {

    @Autowired
    UserDao userDao;
    @Autowired
    CommodityDao commodityDao;
    @Autowired
    CommodityImageDao commodityImageDao;

    @Autowired
    OrderDao orderDao;

    /**
     *  管理员主页
     */

    @RequestMapping(path = "index",method = RequestMethod.GET)

    public ModelAndView admin(){
        return new ModelAndView("/admin/index",null);
    }

    /**
     *
     * 跳转到全部用户页面  http://localhost:8080/second/compus/admin/users
     * @return
     */


    @RequestMapping(path = "user")
    public ModelAndView users()
    {
        return new ModelAndView("admin/user",null);
    }

    @RequestMapping(path = "commodities")
    public ModelAndView commodities(){return new ModelAndView("admin/commodities",null);}

    @RequestMapping(path = "orders")
    public ModelAndView orders(){return new ModelAndView("admin/orders",null);}


    /**
     * 查看全部用户
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path ="listusers")
    @ResponseBody
    public SuccessBean listAllUsers(@RequestParam("offset") final int offset,
                                    @RequestParam("limit")  final int limit,
                                    HttpServletRequest  request,
                                    HttpServletResponse response) {
        // 列出所有的用户
        List<User> userList = userDao.listUsers(offset,limit);
        if(userList == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_NONEUSER);
        /**
         * 管理员查看用户只显示用户的名称、手机号、邮箱、注册时间、性别
         */
        List<UserBean> users = new ArrayList<UserBean>();
        for(int i=0;i<userList.size();i++) {
            User signalUser = userList.get(i);
            UserBean userBean = new UserBean(signalUser,i+1);
            users.add(userBean);
        }
        return new SuccessBean(200,"数据获取成功",null,users);
    }


    /**
     *  删除用户: http://localhost:8080/second/compus/admin/user/delete?user=123456
     * @param userId
     * @return
     */
    @RequestMapping(path = "user/delete")
    @ResponseBody
    public SuccessBean deleteUser(@RequestParam("user") final String userId){

        User user = userDao.findById(userId);
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        userDao.delete(user);
        return new SuccessBean(200,"删除成功");
    }

}


