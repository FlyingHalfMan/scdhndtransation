package com.compus.second.Controller;

import com.compus.second.Constant;
import com.compus.second.Dao.CartDao;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Exception.CartException;
import com.compus.second.Exception.CommodityException;
import com.compus.second.Exception.Enum.CART_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.COMMODITY_EXCEPTION_TYPE;
import com.compus.second.Table.Cart;
import com.compus.second.Table.Commodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cai on 2017/3/23.
 */

@Controller
@RequestMapping(path = "cart")
public class CartController {

    @Autowired
    private  CartDao cartDao;

    @Autowired
    private CommodityDao commodityDao;

    /**
     * 购物车 1。查看购物车 2。添加购物车 3。删除购物车 4。修改购物车
     */

    /**
     * 查看用户的购物车
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView listCart(@RequestParam("offset") final int offset,
                                 @RequestParam("limit") final int limit,
                                 HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        List<Cart> cartList = cartDao.findByUserId(userId);
        return new ModelAndView("cart",null);
    }

    /**
     *  添加商品至购物车
     * @param commodityId
     * @param request
     * @param response
     * @return
     */
    public ModelAndView  addCart(@RequestParam("commodityid") final  String commodityId,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        // 检查该商品是否已经被购买，下订单，商品数量是否充足。加入购物车不算被购买，只有用户下订单才算购买

        String userId = (String) request.getSession().getAttribute("userid");

        // 检查商品的状态，如果商品的状态为不可购买或者下架状态就不允许添加购物车
        Commodity commodity = commodityDao.findByCommodityId(commodityId);
        if (commodity ==null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);
        // 已经卖完了
        if(commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD)
            throw new CartException(CART_EXCEPTION_TYPE.CART_EXCEPTION_COMMODITY_SOLD_OUT);
        Cart cart = new Cart();
        cart.setNumber(1);
        cart.setAddDate(new Date());
        cart.setUserId(userId);
        cart.setCommodityId(commodityId);
        cartDao.addCart(cart);
        return new ModelAndView("",null);
    }

    /**
     * 删除购物车中的商品
     * @param commodityId
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("/delete")
    public ModelAndView deleteCart(@RequestParam("commodityId") final String commodityId,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        return new ModelAndView();
    }


    /**
     * 这个字段待定，很多商品字段不明确
     * @return
     */
//    public ModelAndView updateCart(@RequestParam(""))

    @RequestMapping("delete/all")
    public ModelAndView deleteAllCart(HttpServletRequest request,
                                     HttpServletResponse response){

        return new ModelAndView("",null);
    }


    /**
     * 结算所有的商品
     * @param items
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "settle")
    public ModelAndView settle(@RequestParam("items") final Map items,
                               HttpServletRequest request,
                               HttpServletResponse response){

        return new ModelAndView("",null);
    }


}
