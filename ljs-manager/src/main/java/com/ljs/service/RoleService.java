package com.ljs.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljs.dao.RoleMapper;
import com.ljs.pojo.entity.RoleInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName RoleService
 * @Description: 角色业务层实现类
 * @Author 小松
 * @Date 2019/8/8
 **/
@Service
public class RoleService {

    @Resource
    RoleMapper roleMapper;

    /**
     * 查询角色
     * @param role
     * @param page
     * @param pageSize
     * @return
     */
    public PageInfo<RoleInfo> findRole(String role,Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize);
        return new PageInfo<RoleInfo>(roleMapper.findRole(role));
    }

    /**
     * 新增角色
     * @param roleInfo
     * @return
     */
    public Integer addRole(RoleInfo roleInfo){
        return roleMapper.addRole(roleInfo);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    public Integer deleteRole(Long id){
        return roleMapper.deleteRole(id);
    }

}
