package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.UserInfo;
import com.ljs.service.UserInfoService;
import com.ljs.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author 小松
 * @Date 2019/8/7
 **/
@RestController
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    /**
     * 查询用户
     * @param map
     * @return
     */
    @RequestMapping("findUser")
    public PageInfo<UserInfo> findUser(@RequestBody Map<String,String> map){
        System.out.println(map);
        return userInfoService.findUserInfo(map.get("user"),map.get("sex"),map.get("start"),map.get("end"),Integer.valueOf(map.get("page")),Integer.valueOf(map.get("pageSize")));
    }

    /**
     * 增加用户
     * @param userInfo
     * @return
     */
    @RequestMapping("addUser")
    public ResponseResult addUser(@RequestBody UserInfo userInfo){
        //加密密码
        userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(),"ljs"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(userInfoService.addUser(userInfo) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("增加用户成功");
        }else{
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 修改用户
     * @param userInfo
     * @return
     */
    @RequestMapping("updateUser")
    public ResponseResult updateUser(@RequestBody UserInfo userInfo){
        //加密密码
        userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(),"ljs"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(userInfoService.updateUser(userInfo) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("修改用户成功");
        }else{
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("deleteUser")
    public ResponseResult deleteUser(Long id){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(userInfoService.deleteUser(id) > 0){
            responseResult.setCode(200);
            responseResult.setSuccess("删除用户成功");
        }else{
            responseResult.setCode(500);
        }
        return responseResult;
    }

    @RequestMapping("upload")
    void upload(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        file.transferTo(new File("D:/img/"+originalFilename));
    }

}
