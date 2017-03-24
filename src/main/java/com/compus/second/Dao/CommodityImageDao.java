package com.compus.second.Dao;

import com.compus.second.Table.Commodity;
import com.compus.second.Table.CommodityImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
@Repository
public class CommodityImageDao {

    @PersistenceContext
    private EntityManager entityManager;


    public List<CommodityImage> findByCommodity(String commodityId)
    {
        String sql = "select  c from CommodityImage  as c where  c.commodityId=:commodityId";
        Query query  = entityManager.createQuery(sql).setParameter("commodityId",commodityId);
        return query.getResultList() == null || query.getResultList().size()<1 ?null :  query.getResultList();
    }

    public void add(CommodityImage commodityImages)
    {
        entityManager.persist(commodityImages);
    }

    public void delete(CommodityImage commodityImage)
    {
        entityManager.remove(commodityImage);
    }

}
