package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.MenuInfo;
import com.ljs.pojo.entity.RoleInfo;
import com.ljs.service.MenuService;
import com.ljs.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description: 角色控制层
 * @Author 小松
 * @Date 2019/8/8
 **/
@RestController
@Api(value = "角色相关业务" ,tags = "角色操作相关")
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
    @ApiOperation(value = "查询角色", notes = "查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "role",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "page",
                    required = true,
                    dataType = "integer",
                    dataTypeClass = Integer.class
            ),
            @ApiImplicitParam(
                    name = "pageSize",
                    required = true,
                    dataType = "integer",
                    dataTypeClass = Integer.class
            )
    })
    @RequestMapping("findRole")
    public PageInfo<RoleInfo> findRole(@RequestBody Map<String,String> map){
        PageInfo<RoleInfo> pageInfo = roleService.findRole(Integer.valueOf(map.get("leval")),map.get("role"), Integer.valueOf(map.get("page")), Integer.valueOf(map.get("pageSize")));
        pageInfo.getList().forEach(role->{
            role.setMenuList(menuService.findMenuByRoleId(role.getId()));
        });
        return pageInfo;
    }

    @ApiOperation(value = "查询全部角色", notes = "查询全部角色")
    @RequestMapping("findRoleAll")
    public List<RoleInfo> findRoleAll(@RequestBody Map<String,String> map){
        return roleService.findRoleAll(Integer.valueOf(map.get("leval")));
    }

    /**
     * 新增角色
     * @param roleInfo
     * @return
     */
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @ApiImplicitParam(
            name = "roleInfo",
            required = true,
            dataType = "roleInfo",
            dataTypeClass = RoleInfo.class
    )
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
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(
            name = "id",
            required = true,
            dataType = "long",
            dataTypeClass = Long.class
    )
    @RequestMapping("deleteRole")
    public ResponseResult deleteRole(@RequestBody Map<String,Object> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(roleService.findUserListByRoleId(Long.valueOf(map.get("id").toString())).size()>0){
            responseResult.setCode(500);
            responseResult.setError("已绑定用户，不可删除");
            return responseResult;
        }
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
     * 根据角色等级查询所有权限
     * @return
     */
    @ApiOperation(value = "根据角色等级查询所有权限", notes = "根据角色等级查询所有权限")
    @RequestMapping("findMenuList")
    public List<MenuInfo> findMenuList(@RequestBody Map<String,String> map){
        return menuService.findMenu(Integer.valueOf(map.get("roleId")));
    }

    /**
     * 编辑角色
     * @return
     */
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "ids",
                    required = true,
                    dataType = "array",
                    dataTypeClass = Array.class
            ),
            @ApiImplicitParam(
                    name = "role",
                    required = true,
                    dataType = "role",
                    dataTypeClass = RoleInfo.class
            )
    })
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
