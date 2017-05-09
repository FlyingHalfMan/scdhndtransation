package com.compus.second.Controller;

import com.compus.second.Bean.OrderBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Constant;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.OrderDao;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.ORDER_EXCEPTION_TYPE;
import com.compus.second.Exception.OrderException;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.Order;
import com.compus.second.Table.User;
import com.sun.tools.internal.jxc.ap.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai on 2017/3/24.
 */

@Controller
@RequestMapping(path = "order")
public class OrderController extends BaseController {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private UserDao userDao;


    @RequestMapping("order")
    public ModelAndView order(){
        return new ModelAndView("/shop/order",null);
    }

    @RequestMapping(path = "orders")
    public ModelAndView gotOrderPage(){
        return new ModelAndView("orders",null);
    }


    /**
     * 管理员使用
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("listOrders")
    @ResponseBody
    public SuccessBean listOrders(@RequestParam("offset") final int offset,
                                  @RequestParam("limit") final int limit,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        /*
          管理员的操作只有查看和删除无效的订单
        * 订单显示的信息包括 订单编号，商品的id 商品的名称,商品的数量，订单的价格 订单生成日期，订单状态
        * */
        List<Order> orderList = orderDao.listAllOrders(offset,limit);
        if (orderList == null)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_NOE_ORDERS);

        List<OrderBean> orderBeans = new ArrayList<OrderBean>();
        for (Order order :orderList){
            Commodity commodity = commodityDao.findByCommodityId(order.getCommodityId());
            OrderBean orderBean =new OrderBean(order,commodity);
            orderBeans.add(orderBean);
        }
        return new SuccessBean(200,"数据获取成功",null,orderBeans);
    }

    // 添加订单

    /**
     *  创建新的订单
     * @param userId
     * @return
     */
    @RequestMapping(path = "add",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView  addNeworder(
                                    @RequestParam("commodityId") final String commodityId,
                                    @RequestParam("count") final int count,
                                    @RequestParam("payment") final int payment,
                                    @RequestParam("delivery") final int delivery,
                                    @RequestParam("name") final String name,
                                    @RequestParam("address") final String address,
                                    @RequestParam("contact") final String contact,
                                    @SessionAttribute("userId") final String userId){

        // 1.先检查商品的状态是不是有效
        Commodity commodity = commodityDao.findByCommodityId(commodityId);

        if (commodity == null)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        // 商品已经下架
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_OFF_SALE)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_OFF_SALE);
        // 卖光了
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD_OUT)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_SALD);
        // 商品数量不足
        if (commodity.getCount() < count)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOTENOUGH);

        User user =  userDao.findById(userId);
        Order order  = new Order(commodityId,
                                 commodity.getTitle(),
                                 count,
                           commodity.getPrice() *count,
                                 userId,
                                 payment,
                                 delivery,
                                 address,
                                 contact,
                                 name);
        // 添加数据库
        orderDao.addOrder(order);

        // 修改商品的信息
        commodity.setCount(commodity.getCount() - order.getNumbers());
        if (commodity.getCount() == 0)
            commodity.setStatus(Constant.COMMODITY_STATUS_SOLD_OUT);
        commodityDao.update(commodity);

        return new ModelAndView("redirect:order/success");
       // return new SuccessBean(200,"订单创建成功",new OrderBean(order,commodity),null);
    }



    // 查看用户自己的全部订单

    /**
     *  查看用户订单
     *  http://localhost:8080/second/compus/orders/user?offset=0&limit=10
     * @param offset
     * @param limit
     * @param userId
     * @return
     */

    @RequestMapping(path = "orders/user",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean getOrders(@RequestParam("offset" ) final int offset,
                                 @RequestParam("limit")   final int limit,
                                 @SessionAttribute("userid") final String userId){

       List<Order> orderList =  orderDao.findOrdersByUserId(userId,offset,limit);
       if (orderList == null)
           throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_NOE_ORDERS);

       List<OrderBean> orderBeans = new ArrayList<OrderBean>();
       for (Order order :orderList){
           Commodity commodity = commodityDao.findByCommodityId(order.getCommodityId());
           OrderBean orderBean = new OrderBean(order,commodity);
           orderBeans.add(orderBean);
       }
       return new SuccessBean(200,"数据获取成功",null,orderBeans);
    }

    /**
     * 删除订单
     *  http://localhost:8080/second/compus/delete?orderId=1232131
     *
     * @param orderId
     * @param userId
     * @return
     */

    @RequestMapping(path = "delete",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean deleteOrder(@RequestParam("orderId") final String orderId,
                                   @SessionAttribute("userid") final String userId){

        User user = userDao.findById(userId);
        Order order = orderDao.findOrder(orderId);
        if (order == null)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        // 权限错误
        if (order.getBuyerId() !=userId || user.getAuth() < 2)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_UNAUTHERISIZED);

        // 删除的订单类型只能是已经完成或者已经取消的订单，未付款订单
        if (order.getStatus() != Constant.ORDER_STATUS_WAIT_TO_PAY
                && order.getStatus() !=Constant.ORDER_STATUS_COMPLETE
                && order.getStatus() != Constant.ORDER_STATUS_CANCELED)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_DELETE_FAILED);

        orderDao.deleteOrder(order);
        if (order.getStatus() == Constant.ORDER_STATUS_WAIT_TO_PAY){

            Commodity commodity = commodityDao.findByCommodityId(order.getCommodityId());
            commodity.setCount(commodity.getCount() + order.getNumbers());

            if (commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD_OUT)
                commodity.setStatus(Constant.COMMODITY_STATUS_ON_SALE);
            commodityDao.update(commodity);
        }
        return new SuccessBean(200,"订单删除成功");
    }
}