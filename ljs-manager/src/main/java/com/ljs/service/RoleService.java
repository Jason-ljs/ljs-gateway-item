package com.ljs.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljs.dao.RoleMapper;
import com.ljs.pojo.entity.RoleInfo;
import com.ljs.pojo.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

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
    public PageInfo<RoleInfo> findRole(Integer leval,String role,Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize);
        return new PageInfo<RoleInfo>(roleMapper.findRole(leval,role));
    }

    /**
     * 查询所有角色
     * @return
     */
    public List<RoleInfo> findRoleAll(Integer leval){
        return roleMapper.findRoleAll(leval);
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
    @Transactional
    public Integer deleteRole(Long id){
        roleMapper.deleteRoleUser(id);
        roleMapper.deleteRoleMenu(id);
        return roleMapper.deleteRole(id);
    }

    /**
     * 编辑角色
     * @param menuIds
     * @param roleInfo
     * @return
     */
    @Transactional
    public Integer updateRole(String[] menuIds,RoleInfo roleInfo){
        roleMapper.deleteRoleMenu(roleInfo.getId());
        for (String menuId : menuIds) {
            roleMapper.addRoleMenu(roleInfo.getId(),Long.valueOf(menuId));
        }
        return roleMapper.updateRole(roleInfo);
    }

    /**
     * 根据角色id查询所绑定的用户
     * @param roleId
     * @return
     */
    public List<UserInfo> findUserListByRoleId(Long roleId){
        return roleMapper.findUserListByRoleId(roleId);
    }

}
