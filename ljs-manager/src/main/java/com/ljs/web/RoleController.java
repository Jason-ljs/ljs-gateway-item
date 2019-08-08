package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.RoleInfo;
import com.ljs.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 查询角色
     * @param map
     * @return
     */
    @RequestMapping("findRole")
    public PageInfo<RoleInfo> findRole(@RequestBody Map<String,String> map){
        return roleService.findRole(map.get("role"),Integer.valueOf(map.get("page")),Integer.valueOf(map.get("pageSize")));
    }

    /**
     * 新增角色
     * @param roleInfo
     * @return
     */
    @RequestMapping("addRole")
    public ResponseResult addRole(RoleInfo roleInfo){
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

    @RequestMapping("deleteRole")
    public ResponseResult deleteRole(Long id){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(roleService.deleteRole(id)>0){
            responseResult.setCode(200);
            responseResult.setSuccess("删除角色成功");
        }else {
            responseResult.setCode(500);
            responseResult.setError("新增角色失败");
        }
        return responseResult;
    }

}
