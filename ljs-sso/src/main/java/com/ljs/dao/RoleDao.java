package com.ljs.dao;

import com.ljs.pojo.entity.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @ClassName RoleDao
 * @Description: 权限持久层
 * @Author 小松
 * @Date 2019/8/5
 **/
public interface RoleDao extends JpaRepository<RoleInfo,Long> {

    /**
     * 根据用户ID获取角色信息
     * @param userId
     * @return
     */
    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1",nativeQuery = true)
    public RoleInfo forRoleInfoByUserId(Long userId);

}
