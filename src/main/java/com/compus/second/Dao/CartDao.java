package com.compus.second.Dao;

import com.compus.second.Table.Cart;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cai on 2017/3/23.
 */

@Repository
@Transactional
public class CartDao {

    @PersistenceContext
    private EntityManager entityManager;


    public void addCart(Cart cart){
        entityManager.persist(cart);
    }

    public void deleteCart(Cart cart){
        entityManager.remove(cart);
    }

    public  void updateCart(Cart ca){
        entityManager.merge(ca);
    }

    public Cart findbyCartId(Serializable id){
        return entityManager.find(Cart.class,id);
    }

    /**
     * 查看某个用户的购物车
     * @param userId
     * @return
     */
    public List<Cart> findByUserId(Serializable userId,int offset,int limit){

        String sql = "select  c from Cart as c where c.userId = :userId";
        Query query = entityManager.createQuery(sql).setParameter("userId",userId)
                                                    .setFirstResult(offset)
                                                    .setMaxResults(limit);

        return query.getResultList() == null || query.getResultList().size() < 1 ? null :  query.getResultList();
    }


    public Cart  findByUserIdAndCommodityId(String userId,String commodityId){

        String sql = "select c from Cart  as c where c.userId =:userId and c.commodityId = :commodityId";
        Query query = entityManager.createQuery(sql).setParameter("userId",userId)
                                                    .setParameter("commodityId",commodityId);

        return query.getResultList() == null || query.getResultList().size() < 1 ? null : (Cart) query.getResultList().get(0);
    }



}
