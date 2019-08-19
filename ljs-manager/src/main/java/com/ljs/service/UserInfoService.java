package com.ljs.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljs.dao.UserMapper;
import com.ljs.pojo.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        List<UserInfo> userList = userMapper.findUser(user, start, end, sex);
        for (UserInfo u : userList) {
            u.setRoleInfo(userMapper.findRoleInfoByUserId(u.getId()));
        }
        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userList);
        System.out.println(userInfoPageInfo);
        return userInfoPageInfo;
    }

    /**
     * 根据登录名查询用户
     * @param loginName
     * @return
     */
    public UserInfo findUserByLoginName(String loginName){
        return userMapper.findUserByLoginName(loginName);
    }

    /**
     * 根据手机号查询用户
     * @param tel
     * @return
     */
    public UserInfo findUserByTel(String tel){
        return userMapper.findUserByTel(tel);
    }

    /**
     * 根据邮箱账号查询用户
     * @param email
     * @return
     */
    public UserInfo findUserByEmail(String email){
        return userMapper.findUserByEmail(email);
    }

    /**
     * 添加用户
     * @param userInfo
     * @return
     */
    public Integer addUser(UserInfo userInfo){
        userMapper.editRoleAdd(userInfo.getId(),12L);
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

    /**
     * 修改用户与角色的关系
     * @param uid
     * @param rid
     * @return
     */
    public Integer editRole(Long uid,Long rid){
        userMapper.editRoleDel(uid);
        return userMapper.editRoleAdd(uid,rid);
    }

}
