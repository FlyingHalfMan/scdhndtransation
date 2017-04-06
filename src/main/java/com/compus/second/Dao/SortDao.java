package com.compus.second.Dao;

import com.compus.second.Table.Sorts;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

/**
 * Created by cai on 2017/3/17.
 */
@Repository
@Transactional
public class SortDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据分类名称来获取分类
     * @param name
     * @return
     */
    public Sorts getSortByName(String name) {
       Query query = entityManager.createQuery("select s from Sorts  as s where s.sortName =:name").setParameter("name",name);
        return query.getResultList() == null ||query.getResultList().size() <1 ? null : (Sorts) query.getResultList().get(0);
    }

    /**
     * 根据分类id 来获取分类
     * @param id
     * @return
     */
    public Sorts getSortById(String id) {
        Query query = entityManager.createQuery("select s from Sorts  as s where s.sortId =:id").setParameter("id",id);
        return query.getResultList() == null ||query.getResultList().size() <1 ? null : (Sorts) query.getResultList().get(0);
    }

    public List<Sorts> listAllSorts(){

        Query query = entityManager.createQuery("select  s from Sorts as s");
        return query.getResultList() == null || query.getResultList().size() < 1 ? null : query.getResultList();
    }

    public void addSort(Sorts sorts){
        entityManager.persist(sorts);
    }
}
