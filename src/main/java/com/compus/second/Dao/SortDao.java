package com.compus.second.Dao;

import com.compus.second.Table.Sorts;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

/**
 * Created by cai on 2017/3/17.
 */
@Repository
public class SortDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Sorts getSortByName(String name)
    {
       Query query = entityManager.createQuery("select s from Sorts  as s where s.sortName =:name").setParameter("name",name);
        return query.getResultList() == null ||query.getResultList().size() <1 ? null : (Sorts) query.getResultList().get(0);
    }

    public Sorts getSortById(int id)
    {
        Query query = entityManager.createQuery("select s from Sorts  as s where s.sortId =:id").setParameter("id",id);
        return query.getResultList() == null ||query.getResultList().size() <1 ? null : (Sorts) query.getResultList().get(0);
    }
}
