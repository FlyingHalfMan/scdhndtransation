package com.compus.second.Dao;

import com.compus.second.Constant;
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
     * @return
     */
    public List<Commodity> listCommodityBySortId(int sortId,int offset,int limit,int orderOp) {

        String sql ="";
        switch (orderOp){
            case Constant.COMMODITY_ORDER_BY_DATE_DESC:
                sql = "select  c from Commodity  as c where  c.sortId =:sortid order by c.publishDate desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_DATE_ASC:
                sql = "select  c from Commodity  as c where  c.sortId =:sortid order by c.publishDate asc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_DESC:
                sql = "select  c from Commodity  as c where  c.sortId =:sortid order by c.price desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_ASC:
                sql = "select  c from Commodity  as c where  c.sortId =:sortid order by c.price ASC ";
                break;
                default:
                    sql = "select  c from Commodity  as c ";
                    break;

        }
        Query query = entityManager.createQuery(sql)
                                   .setParameter("sortid",sortId)
                                   .setFirstResult(offset)
                                   .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }


    /**
     * 查看某个用户出售的全部商品（用户详情页面，管理员）
     * @param userId
     * @param offset
     * @param limit
     * @return
     */

    public List<Commodity> listCommodityByUserId(String userId,int order,int offset,int limit) {

        String sql ="";
        switch (order){
            case Constant.COMMODITY_ORDER_BY_DATE_DESC:
                sql = "select  c from Commodity  as c where c.userId =:userId and  order by c.publishDate desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_DATE_ASC:
                sql = "select  c from Commodity  as c where c.userId =:userId and  order by c.publishDate asc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_DESC:
                sql = "select  c from Commodity  as c where c.userId =:userId and  order by c.price desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_ASC:
                sql = "select  c from Commodity  as c where c.userId =:userId and  order by c.price ASC ";
                break;
            default:
                sql = "select  c from Commodity  as c where c.userId =:userId";
                break;
        }
        Query query = entityManager.createQuery(sql)
                .setParameter("userId",userId)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return  query.getResultList() == null || query.getResultList().size() < 1 ?null :query.getResultList();
    }


    /**
     * 搜索商品
     * @param content
     * @param sort
     * @param order
     * @param offset
     * @param limit
     * @return
     */
    public List<Commodity>searchCommodity(String content,int sort,int order,int offset,int limit) {

        String sql ="";
        switch (order){
            case Constant.COMMODITY_ORDER_BY_DATE_DESC:
                sql = "select  c from Commodity  as c where c.detail like %:content% or c.title like %:content% and  c.sortId =:sortid order by c.publishDate desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_DATE_ASC:
                sql = "select  c from Commodity  as c where c.detail like %:content% or c.title like %:content% and  c.sortId =:sortid order by c.publishDate asc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_DESC:
                sql = "select  c from Commodity  as c where c.detail like %:content% or c.title like %:content% and  c.sortId =:sortid order by c.price desc ";
                break;
            case Constant.COMMODITY_ORDER_BY_PRICE_ASC:
                sql = "select  c from Commodity  as c where c.detail like %:content% or c.title like %:content% and  c.sortId =:sortid order by c.price ASC ";
                break;
            default:
                sql = "select  c from Commodity  as c where c.detail like %:content% or c.title like %:content%";
                break;
        }

       Query query = entityManager.createQuery(sql)
                                   .setParameter("content",content)
                                   .setParameter("sortid",sort)
                                   .setFirstResult(offset)
                                   .setMaxResults(limit);
        return query.getResultList() == null || query.getResultList().size() <1 ?null : query.getResultList();
    }


}
