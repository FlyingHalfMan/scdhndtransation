package com.compus.second.Dao;

import com.compus.second.Exception.CommodityException;
import com.compus.second.Exception.Enum.COMMODITY_EXCEPTION_TYPE;
import com.compus.second.Table.Commodity;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */

/**
 *  排序可能出现的组合
 *  1. 不使用排序，使用时间升序排序
 *  2. 时间降序，价格降序，价格升序
 *  3. 排序上不使用组合排序，只允许使用价格排序或者时间排序中的一种
 *  4. 暂时不考虑使用价格区间来进行检索
 * */
/**
 * 商品的主要操作
 * 1. 增加操作，向数据库中添加一件新的商品。
 * 2. 删除商品
 * 3. 修改商品
 * 4. 通过商品的id(commodityId)来查找商品，一般用于查看商品的具体信息
 * 5. 查看全部商品，根据时间升序。offset 和limit 用于翻页操作
 * 6. 查看分类的全部商品
 * 7. 根据关键词，分类，时间排序来查找商品。
 * 8. 根据关键词，时间来查找商品
 * 9. 根据关键词，分类进行查找，排序默认升序。
 */

@Repository
public class CommodityDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *  添加商品
     * @param commodity
     * @throws CommodityException
     */

    public void add(Commodity commodity)throws CommodityException
    {
        try{
            entityManager.persist(commodity);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_ADDFAILED);
        }
    }

    /**
     * 根据商品的id来进行查找
     * @param id
     * @return Commodity
     */


    public Commodity findByCommodityId(Serializable id)
    {
        String sql  = "select  c from Commodity as c where c.commodityId = :id";
        Query query = entityManager.createQuery(sql).setParameter("id",id);
        return query.getResultList() == null || query.getResultList().size() < 1 ?null : (Commodity) query.getResultList().get(0);
    }

    /**
     *
     * @param commodity
     * @throws CommodityException
     */
    public void delete(Commodity commodity) throws CommodityException
    {
        Commodity c = findByCommodityId(commodity.getCommodityId());
        // 查找的商品不存在抛出n删除失败异常
        if (c == null) throw  new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_DELETEFAILED);
        entityManager.remove(c);
    }

    /**
     *
     * @param commodity
     * @throws CommodityException
     */
    public void update(Commodity commodity) throws  CommodityException
    {
        Commodity c = findByCommodityId(commodity.getCommodityId());
        if (c == null) throw  new CommodityException(COMMODITY_EXCEPTION_TYPE.COMMODITY_EXCEPTION_TYPE_DELETEFAILED);
        c = commodity;
        entityManager.persist(c);
    }



    /**
     *  不需要排序条件，排序全部商品，默认按照时间升序(首页，管理员使用)
     * @param offset
     * @param limit
     * @return
     */
    public List<Commodity> listCommodities(int offset,int limit)
    {
        String sql ="select  c from Commodity as c";
        Query query = entityManager.createQuery(sql).setFirstResult(offset).setMaxResults(limit);
        return query.getResultList();
    }

    /**
     *  根据商品的分类来进行排序，默认使用时间升序 ()
     * @param sortId       查找的分类
     * @param offset
     * @param limit
     * @param priceOpt  // 价格排序
     * @param dateOpt   // 时间排序
     * @return
     */
    public List<Commodity> listCommodityBySortId(int sortId,int offset,int limit,String priceOpt,String dateOpt)
    {
        String sql  = "select  c from Commodity  as c where  c.sortId =:sortid order by c.price " + priceOpt +"and c.date "+dateOpt;
        Query query = entityManager.createQuery(sql)
                                   .setParameter("sortid",sortId)
                                   .setFirstResult(offset)
                                   .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }


    /**
     *  查看分类并根据价格来查找
     * @param sortId
     * @param offset
     * @param limit
     * @param priceOpt
     * @return
     */
    public List<Commodity> listCommodityBySortIdSortByPrice(int sortId,int offset,int limit,String priceOpt)
    {
        String sql  = "select  c from Commodity  as c where  c.sortId =:sortid order by c.price " +priceOpt;
        Query query = entityManager.createQuery(sql)
                .setParameter("sortid",sortId)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }

    /**
     *  搜索分类并根据时间来排序，默认使用
     * @param sortId
     * @param offset
     * @param limit
     * @param dateOpt
     * @return
     */
    public List<Commodity> listCommodityBySortIdSortByDate(int sortId,int offset,int limit,String dateOpt)
    {
        if(dateOpt == null || !dateOpt.equals("asc"))  dateOpt ="desc";
        String sql  = "select  c from Commodity  as c where  c.sortId =:sortid order by c.date " +dateOpt;
        Query query = entityManager.createQuery(sql)
                .setParameter("sortid",sortId)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }



    /**
     * 查看某个用户出售的全部商品（用户详情页面，管理员）
     * @param userId
     * @param status 商品的状态，已经售出还是未售出(不填的话默认查询全部)
     * @param offset
     * @param limit
     * @return
     */

    public List<Commodity> listCommodityByUserId(String userId,int status,int offset,int limit)
    {
        String sql ="";
        Query query = null;
        if(status == 0) {
             sql = "select  c from Commodity as c where c.userId =:userId order by c.date asc ";
            query = entityManager.createQuery(sql).setParameter("userId",userId);
        }
        else {
            // status = 1 未出售商品，status =2 已经售出商品
            sql = "select c from Commodity  as c where  c.userId =:userId and c.status=:status order by c.date asc ";
            query= entityManager.createQuery(sql).setParameter("userId",userId).setParameter("status",status);
        }
          query = query.setFirstResult(offset).setMaxResults(limit);
        return  query.getResultList() == null || query.getResultList().size() < 1 ?null :query.getResultList();
    }

    /**
     * 用户进行搜索商品，搜索只搜索商品名称，默认按照时间排序升序排列，可以自行选择，可以选择分类
     * @param content 搜索的内容
     * @param dateOp  排序的方式 asc 升序  desc 降序
     * @return 商品数组 || null
     */
    public List<Commodity>searchCommodity(String content,String dateOp,int sort,int offset,int limit)
    {
        if(dateOp == null || !dateOp.equals("asc"))dateOp="desc";
        String sql  = "select  c from Commodity  as c where  c.title like %:content% and c.sortId =:sortid order by c.date " +dateOp;
        Query query = entityManager.createQuery(sql)
                                   .setParameter("content",content)
                                   .setParameter("sortid",sort)
                                   .setFirstResult(offset)
                                   .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }

    /**
     *     只有搜索内容，不包含分类和自定义时间，默认使用时间升序排序
     * @param content      搜索的内容
     * @param offset       起点
     * @param limit        终点
     * @return
     */
    public List<Commodity>searchCommodity(String content,int offset,int limit)
    {
        String sql  = "select  c from Commodity  as c where  c.title like %:content%  order by c.date asc" ;
        Query query = entityManager.createQuery(sql)
                .setParameter("content",content)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }

    /**
     * 搜索内容并根据时间来排序
     * @param content
     * @param dateOpt
     * @param offset
     * @param limit
     * @return
     */
    public List<Commodity> searchCommodity(String content,String dateOpt,int offset,int limit)
    {
        String sql  = "select  c from Commodity  as c where  c.title like %:content%  order by  c.date "+dateOpt;
        Query query = entityManager.createQuery(sql)
                .setParameter("content",content)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }







}
