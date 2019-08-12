package com.ljs.dao;

import com.ljs.pojo.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName MenuMapper
 * @Description: 权限持久层
 * @Author 小松
 * @Date 2019/8/9
 **/
@Mapper
public interface MenuMapper {

    //查询权限列表
    public List<MenuInfo> findMenu(@Param("leval")Integer leval,@Param("pid") Long pid);

    //根据角色id查询
    public List<Long> findMenuByRoleId(@Param("roleId") Long roleId);

    //添加菜单
    public Integer addMenu(MenuInfo menuInfo);

    //修改菜单
    public Integer updateMenu(MenuInfo menuInfo);

    //删除菜单
    public Integer deleteMenu(MenuInfo menuInfo);

    //根据菜单ID删除菜单与角色中间表
    public Integer deleteMenuRoleByMenuId(@Param("menuId") Long menuId);

}
