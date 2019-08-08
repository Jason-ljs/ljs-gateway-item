package com.ljs.dao;

import com.ljs.pojo.entity.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName MenuDao
 * @Description: 菜单持久层
 * @Author 小松
 * @Date 2019/8/5
 **/
public interface MenuDao extends JpaRepository<MenuInfo,Long> {
    /**
     * 获取角色的菜单信息
     * @return
     */
    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm ON brm.menuId=bm.id where brm.roleId=?1 and bm.leval=?2 ",nativeQuery = true)
    public List<MenuInfo> getFirstMenuInfo(Long roleId, Integer leval);

    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm ON brm.menuId=bm.id where brm.roleId=?1 and bm.leval=?2 and bm.parentId=?3",nativeQuery = true)
    public List<MenuInfo> getFirstMenuInfo(Long roleId, Integer leval, Long id);
}
