package com.ljs.service;

import com.ljs.dao.MenuMapper;
import com.ljs.pojo.entity.MenuInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName MenuService
 * @Description: 权限的业务层类
 * @Author 小松
 * @Date 2019/8/9
 **/
@Service
public class MenuService {

    @Resource
    MenuMapper menuMapper;

    /**
     * 查询全部权限
     * @return
     */
    public List<MenuInfo> findMenu(){
        List<MenuInfo> menuInfoList = menuMapper.findMenu(1,0L);
        //递归填充子列表
        this.getForMenuInfo(menuInfoList);
        return menuInfoList;
    }

    /**
     * 递归查询
     * @param menuInfoList
     */
    public void getForMenuInfo(List<MenuInfo> menuInfoList){

        for(MenuInfo menuInfo:menuInfoList){
            int leval=menuInfo.getLeval() + 1;
            //获取下级的菜单信息
            List<MenuInfo> firstMenuInfo1 = menuMapper.findMenu(leval, menuInfo.getId());
            if(firstMenuInfo1!=null){
                //设置查出来的菜单到父级对象中
                menuInfo.setMenuInfoList(firstMenuInfo1);
                //根据查出来的下级菜单继续查询该菜单包含的子菜单
                getForMenuInfo(firstMenuInfo1);
            }else{
                break;
            }
        }

    }

    /**
     * 根据角色id查询
     * @param roleId
     * @return
     */
    public List<Long> findMenuByRoleId(Long roleId){
        return menuMapper.findMenuByRoleId(roleId);
    }

    /**
     * 添加菜单
     * @param menuInfo
     * @return
     */
    public Integer addMenu(MenuInfo menuInfo){
        return menuMapper.addMenu(menuInfo);
    }

    /**
     * 修改菜单
     * @param menuInfo
     * @return
     */
    public Integer updateMenu(MenuInfo menuInfo){
        return menuMapper.updateMenu(menuInfo);
    }
    /**
     * 删除菜单
     * @param menuInfo
     * @return
     */
    public Integer deleteMenu(MenuInfo menuInfo){
        menuMapper.deleteMenuRoleByMenuId(menuInfo.getId());
        return menuMapper.deleteMenu(menuInfo);
    }

}
