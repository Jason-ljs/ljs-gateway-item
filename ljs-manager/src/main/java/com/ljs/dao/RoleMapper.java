package com.ljs.dao;

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
    public Integer deleteRole(Long id);

}
