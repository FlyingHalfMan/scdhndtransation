package com.compus.second.Dao;

import com.compus.second.Table.CheckRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by cai on 2017/5/8.
 */
@Repository
public class CheckRecordDao {

    @PersistenceContext
    EntityManager entityManager;

    public void add(CheckRecord checkRecord){
        entityManager.persist(checkRecord);
    }

    public CheckRecord findRecordById(int id){

       return  entityManager.find(CheckRecord.class,id);
    }

}
