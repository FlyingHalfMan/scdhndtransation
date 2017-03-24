package com.compus.second.Dao;

import com.compus.second.Table.Order;
import org.omg.CORBA.StringHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by cai on 2017/3/24.
 */
@Repository
public class OrderDao {

    /**
     * 订单处理，包含订单的生成，删除，支付。确认
     * 订单在确认下定后无法进行修改。
     * 一个订单中只能有一种商品，多个商品生成多个订单。
     * 当用户生成订单的时候，需要将商品列表中的商品数量-1 如果商品数量为0 则设置商品状态为不可以购买
     * 当用户取消订单的时候，或者订单超时(自动取消订单)商品的数量自动+1，不可购买的商品显示为可购买。
     * 订单的支付方式分成两种 当面交易和在线交易(支付宝)
     */

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * 添加订单
     * @param order
     */
    @Transactional
    public void addOrder(Order order){

        entityManager.persist(order);
    }

    /**
     * 删除订单(用户如果取消订单的化不属于删除订单)
     * @param order
     */
    @Transactional
    public void deleteOrder(Order order){
        entityManager.remove(order);
    }

    public Order findOrder(String id) {

        String sql = "select  o from  Order  as o where o.orderId = :orderId";
        Query query = entityManager.createQuery(sql).setParameter("orderId",id);
        return query == null || query.getResultList().size() < 1 ? null : (Order) query.getResultList().get(0);
    }

    public void updateOrder(Order order){

        entityManager.merge(order);
    }


    /**
     * 查看用户的全部订单
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Order> listOrdersByUserId(String userId, int offset, int limit){

        String sql = "select o from Order as o where o.userId = :userId order by o.orderDate desc ";
        Query query = entityManager.createQuery(sql).setParameter("userId",userId);
        return query.getResultList() == null || query.getResultList().size() < 1 ? null :query.getResultList();
    }

    /**
     * 搜索订单，搜索的方式包含 订单号查找和订单描述查找
     * @param str
     * @return
     */
    public List<Order> searchOrder(String str){

        String sql = "select  o from Order  as o where o.orderDesc like %:str% or o.orderId like %:str% order by o.orderDate desc ";
        Query query = entityManager.createQuery(sql).setParameter("str",str);

        return query.getResultList() == null || query.getResultList().size() <1 ? null : query.getResultList();
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<Order> findOrdersByUserId(String userId){

        String sql = "select o from Order  as o where  o.userId = :userId order by o.orderDate desc ";
        Query query = entityManager.createQuery(sql).setParameter("userId",userId);
        return query.getResultList() == null || query.getResultList().size() < 1 ? null : query.getResultList();
    }
}
