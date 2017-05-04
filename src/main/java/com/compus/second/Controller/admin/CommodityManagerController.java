package com.compus.second.Controller.admin;

import com.compus.second.Bean.SuccessBean;
import com.compus.second.Dao.CommodityDao;
import com.compus.second.Table.Commodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai on 2017/3/23.
 */

@Controller
@RequestMapping("admin/commodity")
public class CommodityManagerController {

    @Autowired
    CommodityDao commodityDao;
    /**
     * 查看所有的商品
     * @param search 搜索的内容
     * @param sort   排序方式(0,上架时间，1.价格)如果不填写默认使用上架时间降序排序
     * @param asc    是否升序排列(true升序，false 降序)
     * @param sold   是否已经出售，默认是false
     * @param offset
     * @param limit
     */
    @RequestMapping(path = "commodites?search={search}sort={sort}&asc={asc}&sold={sold}&offset={offset}&limit={limit}")
    @ResponseBody
    public SuccessBean listAllCommodities(@RequestParam("search")  String     search,
                                           @RequestParam("sort")    int        sort,
                                           @RequestParam("asc")     boolean    asc,
                                           @RequestParam("sold")    boolean    sold,
                                           @RequestParam("offset")  int        offset,
                                           @RequestParam("limit")   int        limit,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
    {
        List<Commodity> commodityList = null;
        if (limit ==0) limit =offset +10;  // 如果没有填写limit 默认搜索长度是每次10条
        // 空搜索默认搜索全部
        if (search == null && sort ==0) {
            commodityList = commodityDao.listCommodities(offset,limit);
        }

        return new SuccessBean(200,"",null,commodityList);
    }
}
