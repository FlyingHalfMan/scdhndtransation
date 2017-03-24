package com.compus.second.Service;

import com.compus.second.Bean.OrderBean;
import com.compus.second.Constant;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.CommodityImageDao;
import com.compus.second.Dao.OrderDao;
import com.compus.second.Dao.UserDao;
import com.compus.second.Exception.Enum.ORDER_EXCEPTION_TYPE;
import com.compus.second.Exception.OrderException;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.CommodityImage;
import com.compus.second.Table.Order;
import com.compus.second.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai on 2017/3/24.
 */

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private CommodityImageDao commodityImageDao;


    /**
     * 创建订单
     * @param order
     */
    public void  createOrder(Order order){

       String commodityId =  order.getCommodityId();
       Commodity commodity  = commodityDao.findByCommodityId(commodityId);

       // 没有找到商品
       if (commodity == null) throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
       // 商品已经下架
       if (commodity.getStatus() == Constant.COMMODITY_STATUS_OFF_SALE)
           throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_OFF_SALE);

       // 购买的数量超出了商品的实际数量
        if (commodity.getNumber() < order.getNumbers())
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_INVALIDE_COMMODITY_NUMBER);

       // 商品已经出售
       if(commodity.getStatus() == Constant.COMMODITY_STATUS_ORDERED || commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD)
           throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_SALD);

       // 不支持的支付方式
        if(commodity.getPayment() !=Constant.COMODITY_PAYMENT_BOTH && commodity.getPayment() != order.getPayment())
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_INVALIDE_PAYMENT);
        //不支持的配送方式
        if(commodity.getDelivery() != Constant.COMODITY_DELIVERY_BOTH && commodity.getDelivery() != order.getDelivery())
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_INVALIDE_DELIVERY);

        // 关于地址的问题，无法有效的进行验证，所以不做验证

//        保存订单信息
        orderDao.addOrder(order);
        //修改商品的数量
        commodity.setNumber(commodity.getNumber() - order.getNumbers());
        // 商品已经卖光了
        if (commodity.getNumber() == 0)
            commodity.setStatus(Constant.COMMODITY_STATUS_SOLD);
        commodityDao.update(commodity);
    }

    /**
     * 取消订单
     * @param orderId
     */
    public void cancelOrder(String orderId) {

        // 没有找到相应的订单
        Order order = orderDao.findOrder(orderId);
        if (order == null)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        order.setStatus(Constant.ORDER_STATUS_CANCELED);
        orderDao.updateOrder(order);

        //
        Commodity commodity = commodityDao.findByCommodityId(order.getCommodityId());
        commodity.setNumber(commodity.getNumber() + order.getNumbers());
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD)
            commodity.setStatus(Constant.COMMODITY_STATUS_ON_SALE);
        commodityDao.update(commodity);
    }


    /**
     * 删除订单
     * @param orderId
     */
    public void deleteOrder(String orderId){
        Order order = orderDao.findOrder(orderId);
        if (order == null)
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        orderDao.deleteOrder(order);
    }

    /**
     * 查看用户的全部订单
     */

    public List<OrderBean> getOrdersByuserId(String userId){

        List<Order> orderList =  orderDao.findOrdersByUserId(userId);
        if(orderList == null) throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        return parseToOrderBean(orderList);

    }

    /**
     * 参看具体的订单信息
     * @param orderId
     * @return
     */
    public OrderBean findOrderByOrderrId(String orderId){

        Order order = orderDao.findOrder(orderId);
        if (order == null) throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        return parseToOrderBean(order);

    }

    /**
     *
     * @param str
     * @return
     */
    public List<OrderBean> searchORders(String str){

        List<Order> orderList = orderDao.searchOrder(str);
        if (orderList == null )
            throw new OrderException(ORDER_EXCEPTION_TYPE.ORDER_EXCEPTION_COMMODITY_NOT_FOUND);
        return  parseToOrderBean(orderList);
    }

    /**
     * 搜索order
     * @param orderList
     * @return
     */


    private List<OrderBean> parseToOrderBean(List<Order> orderList){

        List<OrderBean> orderBeans = new ArrayList<OrderBean>();
        for(Order order :orderList){

           OrderBean orderBean = parseToOrderBean(order);
            orderBeans.add(orderBean);
        }
        return orderBeans;
    }


    private OrderBean parseToOrderBean(Order order){


            // 设置订单的商品信息
            Commodity commodity = commodityDao.findByCommodityId(order.getCommodityId());
            OrderBean orderBean = new OrderBean();
            orderBean.setCommodityId(commodity.getCommodityId());
            orderBean.setCommodityDesc(commodity.getDescribe());

            //设置图片
            List<CommodityImage> commodityImages = commodityImageDao.findByCommodity(commodity.getCommodityId());
            String image = "";
            if (commodityImages == null)  image = "";
            else  image = commodityImages.get(0).getImageName();
            orderBean.setCommodityImage(image);

            // 设置订单的详细内容
            orderBean.setNumber(order.getNumbers());
            orderBean.setOrderDate(Utils.parseDateToString(order.getOrderDate(),"yyyy-MM-dd HH-mm-ss"));
            orderBean.setOrderId(order.getOrderId());
            orderBean.setPrice(commodity.getPrice());

            String status = "";
            if (order.getStatus() == 0) status = "等待付款";
            else if(order.getStatus() == 1) status = "订单已经取消";
            else  status = "订单完成";
            orderBean.setOrderStatus(status);
            orderBean.setTotalPrice(order.getPrice() * order.getNumbers());
        return orderBean;
    }




}
