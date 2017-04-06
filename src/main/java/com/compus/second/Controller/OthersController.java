package com.compus.second.Controller;

import antlr.StringUtils;
import com.compus.second.Bean.SuccessBean;
import com.compus.second.Dao.SortDao;
import com.compus.second.Table.Sorts;
import com.compus.second.Utils.EncryptUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by cai on 2017/4/4.
 */
@Controller
public class OthersController {

    /**
     * 这里是获取其他一些信息的控制器，例如 分类信息
     */

    @Autowired
    private SortDao sortDao;


    @RequestMapping(path = "other/sorts")
    @ResponseBody
    public SuccessBean  getSorts(HttpServletRequest request,
                                 HttpServletResponse response){

        List<Sorts> sortsList = sortDao.listAllSorts();
        if(sortsList == null)
            throw new RuntimeException("没有找到分类");
        return new SuccessBean(200,"分类获取成功",null,sortsList);
    }

    /**
     * 添加分类，管理员操作，非管理员统统指向404页面
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(path = "other/sorts/add")
    @ResponseBody
    public SuccessBean addSorts(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam("name") final String sortName){

        Sorts sorts = sortDao.getSortByName(sortName);
        if (sorts != null)
            throw new RuntimeException("分类已经存在，请重新指定分类名称");

        sorts = new Sorts();
        sorts.setSortName(sortName);
        sortDao.addSort(sorts);
        return new SuccessBean(200,"分类添加成功");
    }
}
