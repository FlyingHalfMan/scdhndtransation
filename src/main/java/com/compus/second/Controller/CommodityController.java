package com.compus.second.Controller;

import antlr.StringUtils;
import antlr.Utils;
import com.compus.second.Bean.CommodityBean;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Dao.CommodityImageDao;
import com.compus.second.Dao.SortDao;
import com.compus.second.Exception.CommodityException;
import com.compus.second.Exception.Enum.COMMODITY_EXCEPTION_TYPE;
import com.compus.second.Table.Commodity;
import com.compus.second.Table.CommodityImage;
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
public class CommodityController {

    @Autowired
    private SortDao sortDao;

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private CommodityImageDao commodityImageDao;


    /**
     * 跳转到商品出售页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "sell",method = RequestMethod.GET)
    public ModelAndView goToCommodityAddPage(HttpServletRequest request, HttpServletResponse response)
    {

        String commodityId = EncryptUtil.randomString();
        HttpSession session = request.getSession();
        if(session==null) session = request.getSession(true);
        session.setAttribute("commodity",commodityId);
        return new ModelAndView("sell");
    }


    @RequestMapping(path = "add",method = RequestMethod.POST)
    public ModelAndView addCommodity(HttpServletRequest request,HttpServletResponse response,@RequestBody CommodityBean commodityBean)
    {
        /**
         * 用户上传数据的时候，先从session 中去获取commodityId 和已经上传的图片。
         *
         */
        String commodityId = (String) request.getSession().getAttribute("commodity");

        // 检查商品的数据是否合理
        Commodity commodity = new Commodity();
        commodity.setCommodityId(commodityId);              // 设置商品的id
        commodity.setPrice(commodityBean.getPrice());       // 设置商品的价格
        commodity.setTitle(commodityBean.getTitle());       // 设置商品的标题
        commodity.setDescribe(commodityBean.getDescribe()); // 设置商品的描述
        commodity.setSortId(commodityBean.getSortId());     // 设置商品的分类id
        commodity.setDate(new Date());                      // 设置商品发布时间
        commodity.setStatus(1);                             // 设置商品的状态

        commodityDao.add(commodity);                        // 保存到数据库

        // 保存商品图片
        String image01 = (String) request.getSession().getAttribute("imageO1");
        if (image01 !=null && image01.length() >1)
        {
            CommodityImage commodityImage = new CommodityImage(commodityId,image01);
            commodityImageDao.add(commodityImage);
        }
        String image02 = (String) request.getSession().getAttribute("imageO2");
        if(image02 !=null && image02.length() >1)
        {
            CommodityImage commodityImage = new CommodityImage(commodityId,image02);
            commodityImageDao.add(commodityImage);
        }
        String image03 = (String) request.getSession().getAttribute("imageO3");
        if(image03 !=null && image03.length() >1)
        {
            CommodityImage commodityImage = new CommodityImage(commodityId,image03);
            commodityImageDao.add(commodityImage);
        }

        return new ModelAndView("addSuccess",null);
    }

    //上传商品的照片

    /**
     *  图片是一个异步上传的过程，无论是否上传成功都不刷新页面， 图片会上传到图片服务器上,返回的数据中包含imageName 通过拼接链接请求图片
     * @param request
     * @param response
     * @param image
     * @return SuccessBean
     * @throws IOException
     */
    @RequestMapping(path = "/addphoto",method = RequestMethod.POST)
    public SuccessBean uploadPhoto(HttpServletRequest request, HttpServletResponse response, @RequestParam("image")CommonsMultipartFile image) throws IOException {
        HttpSession session = request.getSession();
        String key = (String) session.getAttribute("commodity");
        String imageName = ImageService.uploadImageToImageServer(image, key);
        if (session.getAttribute("image01") == null) {
            session.setAttribute("image01", imageName);
        } else if (session.getAttribute("image02") == null)
        {
            session.setAttribute("image02", imageName);
        }
        else if(session.getAttribute("image03")== null)
        {
            session.setAttribute("image03",imageName);
        }
        else {
            // 图片已经超出添加的最大数量
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_UNABLETOADDIMAGE);
        }
        SuccessBean successBean = new SuccessBean(200,"图片添加成功");
        successBean.setModel(imageName);
        return  successBean;
    }

    @RequestMapping(path = "/{commodityId}",method = RequestMethod.GET)
    public SuccessBean getCommodityWithId(HttpServletRequest request
                                         ,HttpServletResponse response
                                         ,@PathVariable("commodityId") final String commodityId)
    {
       Commodity commodity =  commodityDao.findByCommodityId(commodityId);
        // 找到商品的图片
        List<CommodityImage>  commodityImages =  commodityImageDao.findByCommodity(commodityId);

        List<String> images = new ArrayList<String>();
        for (CommodityImage commodityImage :commodityImages)
        {
            images.add(commodityImage.getImageName());
        }
        //
        CommodityBean commodityBean = new CommodityBean();
        commodityBean.setImages(images);
        commodityBean.setSortName(commodity.getSortName());
        commodityBean.setDescribe(commodity.getDescribe());
        commodityBean.setPrice(commodity.getPrice());
        commodityBean.setTitle(commodity.getTitle());

        // 添加返回数据
        SuccessBean successBean = new SuccessBean(200,"商品信息获取成功");
        successBean.setModel(commodityBean);

        return successBean;
    }


}
