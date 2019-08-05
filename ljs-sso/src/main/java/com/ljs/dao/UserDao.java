package com.ljs.dao;

import com.ljs.pojo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName UserDao
 * @Description: 用户持久层
 * @Author 小松
 * @Date 2019/8/5
 **/
public interface UserDao extends JpaRepository<UserInfo,Long> {

    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public UserInfo findByLoginName(String loginName);


}
