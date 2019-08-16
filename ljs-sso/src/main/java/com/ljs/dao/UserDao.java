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

    //根据手机号查询用户
    @Query(value = "select * from base_user where tel=?1",nativeQuery = true)
    public UserInfo findUserByTel(String tel);

    //根据code码查询用户
    @Query(value = "select * from base_user where code=?1",nativeQuery = true)
    public UserInfo findUserByCode(String code);

    //根据code码修改密码
    @Query(value = "update base_user set password=?1 where code=?2",nativeQuery = true)
    public void updatePasswordByCode(String password,String code);

    //根据用户ID修改code码
    @Query(value = "update base_user set code=?1 where id=?2",nativeQuery = true)
    public void updateCodeById(Long id,String code);

}
