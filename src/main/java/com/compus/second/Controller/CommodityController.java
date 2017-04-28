package com.compus.second.Controller;

import antlr.StringUtils;
import antlr.Utils;
import com.compus.second.Bean.CommodityBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Constant;
import com.compus.second.Dao.*;
import com.compus.second.Exception.CommodityException;
import com.compus.second.Exception.Enum.COMMODITY_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.INVALID_EXCEPTION_TYPE;
import com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE;
import com.compus.second.Exception.InvalidException;
import com.compus.second.Exception.UserException;
import com.compus.second.Table.*;
import com.compus.second.Utils.EncryptUtil;
import com.compus.second.Utils.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
@Controller
@RequestMapping("commodity")
public class CommodityController extends BaseController{

    @Autowired
    private SortDao sortDao;
    @Autowired
    private CommodityDao commodityDao;
    @Autowired
    private CommodityImageDao commodityImageDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;


    /**
     * 跳转到商品出售页面,用户出售页面
     * http://localhost/second/commodity/sell
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "sell",method = RequestMethod.GET)
    public ModelAndView goToCommodityAddPage(HttpServletRequest request,
                                             HttpServletResponse response) {

        String commodityId = EncryptUtil.randomString();
        HttpSession session = request.getSession();
        if(session==null) session = request.getSession(true);
        session.setAttribute("commodity",commodityId);
        return new ModelAndView("sell");
    }


    //上传商品的照片

    /**
     * 图片是一个异步上传的过程，无论是否上传成功都不刷新页面， 图片会上传到图片服务器上,返回的数据中包含imageName 通过拼接链接请求图片
     * http://localhost/second/commodity/addphoto
     * @param request
     * @param response
     * @param image
     * @return SuccessBean
     * @throws IOException
     */
    @RequestMapping(path = "/addphoto",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean uploadPhoto(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam(value = "image") final CommonsMultipartFile image) throws IOException {

        // 获取session中的图片中的商品信息
        HttpSession session = request.getSession();
        String key = (String) session.getAttribute("commodity");
        String imageName = ImageService.uploadImageToImageServer(image, key);
        if (session.getAttribute("image01") == null) {
            session.setAttribute("image01", imageName);
        } else if (session.getAttribute("image02") == null) {
            session.setAttribute("image02", imageName);
        }
        else if(session.getAttribute("image03")== null) {
            session.setAttribute("image03",imageName);
        }
        else {
            // 图片已经超出添加的最大数量
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_UNABLETOADDIMAGE);
        }
        // 这边图片不进行保存等到下一步添加商品具体信息的时候再添加

        //返回保存成功信息
        return new SuccessBean(200,"图片添加成功");

    }

    /**
     * 用户上传商品的信息
     * @param request
     * @param response
     * @param commodityBean
     * @return
     */
    @RequestMapping(path = "add",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean addCommodity(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody CommodityBean commodityBean) {
        /**
         * 用户上传数据的时候，先从session 中去获取commodityId 和已经上传的图片。
         */
        String commodityId = (String) request.getSession().getAttribute("commodity");

        // 检查商品的数据是否合理
        Commodity commodity = new Commodity();
        commodity.setCommodityId(commodityId);                              // 设置商品的id
        commodity.setPrice(commodityBean.getPrice());                       // 设置商品的价格
        commodity.setTitle(commodityBean.getTitle());                       // 设置商品的标题
        commodity.setDetail(commodityBean.getDescribe());                   // 设置商品的描述

        // 设置商品的分类信息
        commodity.setSortId(commodityBean.getSortId());                     // 设置商品的分类id
        Sorts sorts = sortDao.getSortById(commodityBean.getSortId());
        commodity.setSortName(sorts.getSortName());

        commodity.setPublishDate(new Date());                               // 设置商品发布时间
        commodity.setStatus(Constant.COMMODITY_STATUS_ON_SALE);             // 设置商品的状态

        // 保存商品图片
        HttpSession session  = request.getSession();
        String image01 = (String) session.getAttribute("imageO1");
        if (image01 != null && image01.length() > 1) {
            CommodityImage commodityImage = new CommodityImage(commodityId,image01);
            commodityImageDao.add(commodityImage);
        }
        String image02 = (String) session.getAttribute("imageO2");

        if(image02 != null && image02.length() > 1) {
            CommodityImage commodityImage = new CommodityImage(commodityId,image02);
            commodityImageDao.add(commodityImage);
        }
        String image03 = (String) session.getAttribute("imageO3");

        if(image03 != null && image03.length() > 1) {
            CommodityImage commodityImage = new CommodityImage(commodityId,image03);
            commodityImageDao.add(commodityImage);
        }

        // 设置商品的用户信息
        String userId = (String) session.getAttribute("userid");
        commodity.setUserId(userId);

        // 保存到数据库
        commodityDao.add(commodity);

        return new SuccessBean(200,"商品添加成功");
    }


    /**
     * 获取某件商品的详细信息
     * @param request
     * @param response
     * @param commodityId
     * @return
     */
    @RequestMapping(path = "/{commodityId}",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean getCommodityWithId(HttpServletRequest request
                                         ,HttpServletResponse response
                                         ,@PathVariable("commodityId") final String commodityId) {

        // 查找商品信息
        Commodity commodity =  commodityDao.findByCommodityId(commodityId);
        // 没有找到商品
        if (commodity == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);

        // 找到商品的图片
        List<String>  commodityImages =  commodityImageDao.findByCommodity(commodityId);

        // 设置返回参数
        User user = userDao.findById(commodity.getUserId());
        if (user == null)
            throw new UserException(USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);
        CommodityBean commodityBean = new CommodityBean(commodity,commodityImages,user);

        return new SuccessBean(200,"商品信息获取成功",commodityBean,null);
    }


    /**
     * 删除商品
     * @param commodityId
     * @return
     */
    @RequestMapping(path = "delete")
    @ResponseBody
    public SuccessBean deleteCommodity(@RequestParam("id") final String commodityId){

       Commodity commodity = commodityDao.findByCommodityId(commodityId);
       if (commodity == null)
           throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);
       commodityDao.delete(commodity);

       // 如果商品已经被下定了则无法删除
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_ORDERED)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_ORDERED);

       return new SuccessBean(200,"商品已经删除");
    }


    /***
     *  商品修改只允许修改商品的价格
     *  http://localhost/second/commodity/update/price?commodity=123123&price=12.12
     * @return
     */
    @RequestMapping(path = "update/price",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean updateCommodityPrice(@RequestParam("price")final float price,
                                       @RequestParam("commodity") final String id){

        Commodity commodity = commodityDao.findByCommodityId(id);
        //先判断商品是否存在
        if (commodity == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);

        commodity.setPrice(price);
        commodityDao.update(commodity);

        //如果商品已经被人下了订单，就需要修改订单中的价格
        List<Order> orderList = orderDao.findOrderByCommodityId(id);
        if (orderList !=null){
            for (Order order : orderList){
                if (order.getStatus() == Constant.ORDER_STATUS_WAIT_TO_PAY ) {
                    order.setPrice(order.getNumbers() * price);
                    orderDao.updateOrder(order);
                }
            }
        }
        CommodityBean commodityBean = new CommodityBean(commodity,
                                                        commodityImageDao.findByCommodity(id),
                                                        userDao.findById(commodity.getUserId()));

        return new SuccessBean(200,"商品信息更新成功",commodityBean,null);
    }


    /**
     *  更新商品的数量
     *  http://localhost/second/commodity/update/number?id=123123&number=1
     * @param number
     * @param id
     * @return
     */

    @RequestMapping(value = "update/number",method = RequestMethod.POST)
    @ResponseBody
    public SuccessBean updateCommodityNumbers(@RequestParam("number") final int number,
                                              @RequestParam("id") final String id){

        // 检查 number的数量是否正确
        if (number <= 0)
            throw new InvalidException(INVALID_EXCEPTION_TYPE.INVALID_EXCEPTION_INVALIDE_NUMBER);
        Commodity commodity = commodityDao.findByCommodityId(id);

        //先判断商品是否存在
        if (commodity == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_OFF_SALE)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_OFF_SALE);

        // 修改数量
        commodity.setCount(number);

        // 如果商品当前的的状态是已经售空，需要修改商品的状态
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_SOLD_OUT){
            commodity.setStatus(Constant.COMMODITY_STATUS_ON_SALE);
        }
        // 更新数据库
        commodityDao.update(commodity);

        CommodityBean commodityBean = new CommodityBean(commodity,
                commodityImageDao.findByCommodity(id),
                userDao.findById(commodity.getUserId()));
        return new SuccessBean(200,"商品价格更新成功",commodityBean,null);
    }


    /**
     * 下架或者重新上架商品
     *  http://localhost/commodity/update/satus?id=123123
     * @param id
     * @return
     */
    @RequestMapping("update/satus")
    public SuccessBean updateCommodityStatus(@RequestParam("id") final String id){

        Commodity commodity = commodityDao.findByCommodityId(id);

        if (commodity == null )
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_NOTFOUND);
        // 用户下架商品，要判断商品是否已经被下定，如果被下定了就不允许下架
        if (commodity.getStatus() == Constant.COMMODITY_STATUS_ON_SALE) {
            List<Order> orderList = orderDao.findOrderByCommodityId(id);
            if (orderList !=null)
                throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_ORDERED);
            commodity.setStatus(Constant.COMMODITY_STATUS_OFF_SALE);
        }
        else if(commodity.getStatus() == Constant.COMMODITY_STATUS_OFF_SALE)
            commodity.setStatus(Constant.COMMODITY_STATUS_ON_SALE);

        CommodityBean commodityBean = new CommodityBean(commodity,
                commodityImageDao.findByCommodity(id),
                userDao.findById(commodity.getUserId()));
        return new SuccessBean(200,"操作成功",commodityBean,null);
    }


    /**
     * 获取商品信息 http://localhost:8080/second/compus/admin/commodities?offset=0&limit=10
     * 跳转到商品管理页面
     * @return
     */
    @RequestMapping(path = "commodity",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView goToCommodityPage(){
        return new ModelAndView("commodities",null);
    }


    /**
     * 查看全部商品
     *  http://localhost/second/commodity/commodities?order=0&offset=0&limit=10
     * @param request
     * @param response
     * @param offset
     * @param limit
     * @return
     */

    @RequestMapping(path = "commodities",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean getCommodities(HttpServletRequest  request,
                                      HttpServletResponse response,
                                      @RequestParam("order")  final int order,
                                      @RequestParam("offset") final int offset,
                                      @RequestParam("limit")  final int limit){

        List<Commodity> commodityList = commodityDao.listCommodities(offset,limit);
        if (commodityList == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_EMPTY);

        List<CommodityBean> commodityBeans = new ArrayList<CommodityBean>();

        for (Commodity commodity :commodityList){
            CommodityBean commodityBean = parseCommodityBean(commodity);
            commodityBeans.add(commodityBean);
        }

        return new SuccessBean(200,"数据获取成功",null,commodityBeans);
    }


    /**
     * 根据分类查看
     *  http://localhost/second/commodity/sort/1?order=date&offset=0&limit=10
     * @param sortid
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(path = "commodities/{sort}")
    @ResponseBody
    public SuccessBean getCommoditiesBySortId(@PathVariable("sort")   final int sortid,
                                              @RequestParam("order")  final int order,
                                              @RequestParam("offset") final int offset,
                                              @RequestParam("limit")  final int limit){

        List<Commodity>commodityList = commodityDao.listCommodityBySortId(sortid,offset,limit,order);
        if (commodityList == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_EMPTY);

        List<CommodityBean> commodityBeans = new ArrayList<CommodityBean>();

        for (Commodity commodity :commodityList){
            CommodityBean commodityBean = parseCommodityBean(commodity);
            commodityBeans.add(commodityBean);
        }

        return new SuccessBean(200,"数据获取成功",null,commodityBeans);
    }


    /**
     * 搜索
     * http://localhost/second/commodity/search?content=23234&sort=0&order=0&offset=0&limit=10
     * @param content   搜索的内容
     * @param sort      分类
     * @param order     排序方式
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(path = "search",method = RequestMethod.GET)
    @ResponseBody
    public SuccessBean searchCommodity(@RequestParam("content")final  String content,
                                       @RequestParam(value = "sort",defaultValue = "-1") final int sort,
                                       @RequestParam("order")   final int order,
                                       @RequestParam("offset")  final int offset,
                                       @RequestParam("limit")   final int limit){

        List<Commodity>commodityList = commodityDao.searchCommodity(content,sort,order,offset,limit);
        if (commodityList == null)
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_EMPTY);

        List<CommodityBean> commodityBeans = new ArrayList<CommodityBean>();
        for (Commodity commodity :commodityList){

            CommodityBean commodityBean = parseCommodityBean(commodity);
            commodityBeans.add(commodityBean);
        }
        return new SuccessBean(200,"数据获取成功",null,commodityBeans);
    }

    //
    private CommodityBean parseCommodityBean(Commodity commodity){

        List<String> images = commodityImageDao.findByCommodity(commodity.getCommodityId());
        User user = userDao.findById(commodity.getUserId());
        CommodityBean commodityBean = new CommodityBean(commodity,images,user);
        return commodityBean;
    }
}