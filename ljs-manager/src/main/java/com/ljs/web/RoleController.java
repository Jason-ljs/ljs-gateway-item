package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.MenuInfo;
import com.ljs.pojo.entity.RoleInfo;
import com.ljs.service.MenuService;
import com.ljs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description: 角色控制层
 * @Author 小松
 * @Date 2019/8/8
 **/
@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    /**
     * 查询角色
     * @param map
     * @return
     */
    @RequestMapping("findRole")
    public PageInfo<RoleInfo> findRole(@RequestBody Map<String,String> map){
        PageInfo<RoleInfo> pageInfo = roleService.findRole(map.get("role"), Integer.valueOf(map.get("page")), Integer.valueOf(map.get("pageSize")));
        pageInfo.getList().forEach(role->{
            role.setMenuList(menuService.findMenuByRoleId(role.getId()));
        });
        return pageInfo;
    }

    @RequestMapping("findRoleAll")
    public List<RoleInfo> findRoleAll(){
        return roleService.findRoleAll();
    }

    /**
     * 新增角色
     * @param roleInfo
     * @return
     */
    @RequestMapping("addRole")
    public ResponseResult addRole(@RequestBody RoleInfo roleInfo){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(roleService.addRole(roleInfo)>0){
            responseResult.setCode(200);
            responseResult.setSuccess("新增角色成功！");
        }else {
            responseResult.setCode(500);
            responseResult.setError("新增角色失败");
        }
        return responseResult;
    }

    /**
     * 删除角色
     * @param map
     * @return
     */
    @RequestMapping("deleteRole")
    public ResponseResult deleteRole(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(roleService.deleteRole(Long.valueOf(map.get("id").toString()))>0){
            responseResult.setCode(200);
            responseResult.setSuccess("删除角色成功");
        }else {
            responseResult.setCode(500);
            responseResult.setError("删除角色失败");
        }
        return responseResult;
    }

    /**
     * 查询所有权限
     * @return
     */
    @RequestMapping("findMenuList")
    public List<MenuInfo> findMenuList(){
        return menuService.findMenu();
    }

    /**
     * 编辑角色
     * @return
     */
    @RequestMapping("updateRole")
    public ResponseResult updateRole(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String[] menuIds = map.get("ids").toString().split(",");
        Map p = (Map) map.get("role");
        RoleInfo role = new RoleInfo();
        role.setId(Long.valueOf(p.get("id").toString()));
        role.setRoleName(p.get("roleName").toString());
        role.setMiaoShu(p.get("miaoShu").toString());
        if(roleService.updateRole(menuIds,role) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("编辑绑定权限成功！");
        }else{
            responseResult.setCode(500);
            responseResult.setError("编辑绑定权限失败");
        }
        return responseResult;
    }

}
