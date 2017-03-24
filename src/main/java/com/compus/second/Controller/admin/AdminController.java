package com.compus.second.Controller.admin;

import com.compus.second.Controller.BaseController;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.User;
import com.compus.second.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by cai on 2017/3/18.
 */
@RequestMapping(path = "/admin")
@Controller
public class AdminController extends BaseController {

    @Autowired
    UserDao userDao;

    /**
     * 跳转到全部用户页面  http://localhost:8080/second/compus/admin/users
     * @return
     */
    @RequestMapping(path = "users")
    public ModelAndView users()
    {
        return new ModelAndView("users",null);
    }


    /**
     * 返回全部的用户数据  http://localhost:8080/second/compus/listusers?offset=0&limit=10;
     *
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return ModelAndView
     */

    @RequestMapping(path ="listusers")
    public ModelAndView listAllUsers(@RequestParam("offset") final int offset,
                                     @RequestParam("limit") final int limit,
                                     HttpServletRequest request, HttpServletResponse response) {
        // 列出所有的用户
        List<User> userList = userDao.listUsers();
        if(userList == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_NONEUSER);
        /**
         * 管理员查看用户只显示用户的名称、手机号、邮箱、注册时间、性别
         */
        List<Map<String,Object>> users = new ArrayList<Map<String, Object>>();
        for(int i=0;i<userList.size();i++) {
            User signalUser = userList.get(i);
            Map<String, Object> user = new HashMap<String, Object>();
            user.put("id",i++);
            user.put("userId",signalUser.getUserId());
            user.put("userName",signalUser.getName());
            user.put("gender",signalUser.getGender() ==null?"未设置":signalUser.getGender());
            user.put("mobile",signalUser.getMobile() ==null?"未设置":signalUser.getMobile());
            user.put("email",signalUser.getEmail()==null?"未设置":signalUser.getEmail());
            user.put("registDate",signalUser.getRegistDate());
            users.add(user);
        }
        return new ModelAndView("users", (Map<String, ?>) new HashMap<String,List>().put("users",users));
    }


    /**
     *  删除用户: http://localhost:8080/second/compus/admin/user/delete?user=123456
     * @param userId
     * @return
     */
    @RequestMapping(path = "-user/delete")
    public ModelAndView deleteUser(@RequestParam("user") final String userId){

        User user = userDao.findById(userId);
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        userDao.delete(user);
        return new ModelAndView("users",null);
    }




}
