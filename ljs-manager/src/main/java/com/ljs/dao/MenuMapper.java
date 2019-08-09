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

}
