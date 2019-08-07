package com.ljs.dao;


import com.ljs.pojo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description: 用户持久层
 * @Author 小松
 * @Date 2019/8/7
 **/
@Mapper
public interface UserMapper {

    //查询用户
    public List<UserInfo> findUser(@Param("user") String user, @Param("start")String start,@Param("end") String end,@Param("sex") String sex);

    //增加用户
    public Integer addUser(UserInfo userInfo);

    //修改用户
    public Integer updateUser(UserInfo userInfo);

    //删除用户
    public Integer deleteUser(@Param("id") Long id);

    //删除用户的角色
    public Integer deleteRoleByUserId(@Param("id") Long id);
}