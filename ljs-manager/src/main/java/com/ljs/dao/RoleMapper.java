package com.ljs.dao;

import com.ljs.pojo.entity.MenuInfo;
import com.ljs.pojo.entity.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName RoleMapper
 * @Description: 角色持久层
 * @Author 小松
 * @Date 2019/8/8
 **/
@Mapper
public interface RoleMapper {

    //查询角色
    public List<RoleInfo> findRole(@Param("role") String role);

    //新增角色
    public Integer addRole(RoleInfo roleInfo);

    //删除角色
    public Integer deleteRole(@Param("id") Long id);
    //删除角色与用户中间表
    public Integer deleteRoleUser(@Param("roleId") Long roleId);
    //删除角色与权限中间表
    public Integer deleteRoleMenu(@Param("roleId") Long roleId);

    //增加角色与权限中间表
    public Integer addRoleMenu(@Param("roleId") Long roleId,@Param("menuId") Long menuId);

    //修改角色信息
    public Integer updateRole(Object roleInfo);

}
