package com.ljs.web;

import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.MenuInfo;
import com.ljs.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MenuController
 * @Description: 菜单控制层
 * @Author 小松
 * @Date 2019/8/10
 **/
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    /**
     * 添加菜单
     * @param menuInfo
     * @return
     */
    @RequestMapping("addMenu")
    public ResponseResult addMenu(@RequestBody MenuInfo menuInfo){
        menuInfo.setLeval(menuInfo.getLeval()+1);
        menuInfo.setParentId(menuInfo.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(menuService.addMenu(menuInfo) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("添加菜单成功");
        }else {
            responseResult.setCode(500);
            responseResult.setError("添加菜单失败");
        }
        return responseResult;
    }

    /**
     * 修改菜单
     * @param menuInfo
     * @return
     */
    @RequestMapping("updateMenu")
    public ResponseResult updateMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(menuService.updateMenu(menuInfo) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("修改菜单成功");
        }else {
            responseResult.setCode(500);
            responseResult.setError("修改菜单失败");
        }
        return responseResult;
    }

    /**
     * 删除菜单
     * @param menuInfo
     * @return
     */
    @RequestMapping("deleteMenu")
    public ResponseResult deleteMenu(@RequestBody MenuInfo menuInfo){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(menuService.deleteMenu(menuInfo) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("删除菜单成功");
        }else {
            responseResult.setCode(500);
            responseResult.setError("删除菜单失败");
        }
        return responseResult;
    }

}
