package com.compus.second.Controller;

import com.compus.second.Bean.CartCommodity;
import com.compus.second.Bean.SuccessBean;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by cai on 2017/3/23.
 */

@Controller
public class CartController extends BaseController {

    @Autowired
    private  CartDao cartDao;

    @Autowired
    private CommodityDao commodityDao;

    /**
     * 购物车 1。查看购物车 2。添加购物车 3。删除购物车 4。修改购物车
     */



    @RequestMapping(path = "cart",method = RequestMethod.GET)
    public String  cart(){
        return "cart";
    }

    /**
     * 查看用户的购物车
     * @param offset
     * @param limit
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(path = "cart/list",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean listCart(@RequestParam("offset") final int offset,
                                @RequestParam("limit") final int limit,
                                HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        List<Cart> cartList = cartDao.findByUserId(userId,offset,limit);
        if (cartList == null)
            throw new CartException(CART_EXCEPTION_TYPE.CART_EXCEPTION_CART_EMPTY);

        List<CartCommodity> cartCommodities = new ArrayList<CartCommodity>();
        for (Cart cart :cartList){
            Commodity commodity = commodityDao.findByCommodityId(cart.getCommodityId());
            CartCommodity  cartCommodity = new CartCommodity(commodity,cart);
            cartCommodities.add(cartCommodity);
        }

        return new SuccessBean(200,"信息获取成功",null,cartCommodities);
    }

    /**
     *  添加商品至购物车
     * @param commodityId
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("cart/add")
    @ResponseBody
    public SuccessBean  addCart(@RequestParam("commodityid") final  String commodityId,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        // 检查该商品是否已经被购买，下订单，商品数量是否充足。加入购物车不算被购买，只有用户下订单才算购买

        String userId = (String) request.getSession().getAttribute("userId");

        // 检查商品的状态，如果商品的状态为不可购买或者下架状态就不允许添加购物车
        Commodity commodity = commodityDao.findByCommodityId(commodityId);
        if (commodity ==null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);

        // 已经卖完了
        if(commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD)
            throw new CartException(CART_EXCEPTION_TYPE.CART_EXCEPTION_COMMODITY_SOLD_OUT);

        // 商品已经下架
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD_OUT)
            throw new CartException(CART_EXCEPTION_TYPE.CART_EXCEPTION_COMMODITY_OFF_SALE);

        Cart cart = new Cart();
        cart.setNumber(1);
        cart.setAddDate(new Date());
        cart.setUserId(userId);
        cart.setCommodityId(commodityId);
        cartDao.addCart(cart);
        return new SuccessBean(200,"添加购物车成功","http://localhost:8080/second/addSuccess.html?commodityId="+commodityId,null);
    }

    /**
     * 删除购物车中的商品
     * @param commodityId
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("cart/delete")
    @ResponseBody
    public SuccessBean deleteCart(@RequestParam("commodityId") final String commodityId,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   @SessionAttribute("userid") final String userId){
        Cart cart = cartDao.findByUserIdAndCommodityId(userId,commodityId);
        if (cart == null)
            throw new CartException(CART_EXCEPTION_TYPE.CART_EXCEPTION_COMMODITY_NOT_FOUND);
        cartDao.deleteCart(cart);
        return new SuccessBean(200,"删除成功");
    }


    /**
     * 这个字段待定，很多商品字段不明确
     * @return
     */
//    public ModelAndView updateCart(@RequestParam(""))


    /**
     * 清空购物车
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("cart/delete/all")
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

    @RequestMapping(path = "addSuccess")
    public String addSuccess(){
        return "addSuccess";
    }

}
