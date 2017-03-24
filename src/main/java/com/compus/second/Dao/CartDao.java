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
public class CartDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addCart(Cart cart){
        entityManager.persist(cart);
    }

    @Transactional
    public void deleteCart(Cart cart){
        entityManager.remove(cart);
    }

    @Transactional
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
    public List<Cart> findByUserId(Serializable userId){

        String sql = "select  c from Cart as c where c.userId = :userId";
       Query query = entityManager.createQuery(sql).setParameter("userId",userId);
       return query.getResultList() == null || query.getResultList().size() < 1 ? null :  query.getResultList();
    }



}
