package com.compus.second.Dao;

import com.compus.second.Table.User;
import com.compus.second.Utils.Utils;
import com.compus.second.Exception.UserException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

import static com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_INVALIDCOUNT;
import static com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_NOTRIGST;
import static com.compus.second.Exception.Enum.USER_EXCEPTOIN_TYPE.USER_EXCEPTOIN_TYPE_USER_NOT_FOUND;

/**
 * Created by cai on 2017/3/16.
 */
@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public  void addUser(User user)
    {
        entityManager.persist(user);
    }

    @Transactional
    public void delete(User user)
    {
        entityManager.remove(user);
    }

    public void update(User user)
    {
        entityManager.merge(user);
    }


    /**
     * 查看用户的具体信息
     * @param userId
     * @return
     * @throws Exception
     */
    public User findById(Serializable userId) throws  UserException
    {
        String sql = "select u from User as u where u.userId = :userid";
        Query query = entityManager.createQuery(sql).setParameter("userid",userId);

        if(query.getResultList() == null || query.getResultList().size() < 1)
            throw new UserException(USER_EXCEPTOIN_TYPE_USER_NOT_FOUND);

        return (User) query.getResultList().get(0);
    }


    public List<User> findUserByUserName(String name){

        String sql = "select u from User as u where u.name = :name";
        Query query = entityManager.createQuery(sql);
        return query.getResultList() == null || query.getResultList().size() < 1 ? null :query.getResultList();
    }

    /**
     *  通过账号(邮箱，手机)查询用户
     * @param count
     * @return
     * @throws UserException
     */

    public User findByCount(String count) throws UserException{
        String sql = "";
        if (Utils.isRightEmail(count))
        {
            sql = "select u from User  as u where  u.email =:count";
        }
        else if(Utils.isRightMobile(count))
        {
            sql="select  u from User as u where  u.mobile =:count";
        }
        else {
            throw new UserException(USER_EXCEPTOIN_TYPE_INVALIDCOUNT.getCode(),USER_EXCEPTOIN_TYPE_INVALIDCOUNT.getMsg());
        }

        Query query =  entityManager.createQuery(sql).setParameter("count",count);
        return query.getResultList() == null || query.getResultList().size() < 1? null : (User) query.getResultList().get(0);
    }

    /**
     * 查看所有的用户
     */
    public List<User> listUsers()
    {
        String sql = "select u from User as u";
        Query query = entityManager.createQuery(sql);
        return query.getResultList()== null || query.getResultList().size() <1 ?null: query.getResultList();
    }

}
