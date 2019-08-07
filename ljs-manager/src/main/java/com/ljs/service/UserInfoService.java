package com.ljs.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljs.dao.UserMapper;
import com.ljs.pojo.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName UserInfoService
 * @Description: 用户业务层实现类
 * @Author 小松
 * @Date 2019/8/7
 **/
@Service
public class UserInfoService {

    @Resource
    UserMapper userMapper;

    /**
     * 查询用户列表
     * @param user
     * @param sex
     * @param start
     * @param end
     * @param page
     * @param pageSize
     * @return
     */
    public PageInfo<UserInfo> findUserInfo(String user, String sex, String start, String end, Integer page, Integer pageSize){
        PageHelper.startPage(page,pageSize);
        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userMapper.findUser(user, start, end, sex));
        System.out.println(userInfoPageInfo);
        return userInfoPageInfo;
    }

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    public Integer addUser(UserInfo userInfo){
        return userMapper.addUser(userInfo);
    }

    /**
     * 修改用户
     * @param userInfo
     * @return
     */
    public Integer updateUser(UserInfo userInfo){
        return userMapper.updateUser(userInfo);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    public Integer deleteUser(Long id){
        //从中间表中删除该用户的角色
        userMapper.deleteRoleByUserId(id);
        return userMapper.deleteUser(id);
    }

}