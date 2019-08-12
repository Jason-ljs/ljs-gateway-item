package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.ljs.excel.UserEcxcelUtils;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.UserInfo;
import com.ljs.service.UserInfoService;
import com.ljs.utils.MD5;
import com.ljs.utils.UID;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author 小松
 * @Date 2019/8/7
 **/
@RequestMapping("userSwagger")
@RestController
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    List<UserInfo> list = null;

    /**
     * 查询用户
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "查询用户列表", notes = "分页模糊查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "user",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "sex",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "start",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "end",
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
    @RequestMapping("findUser")
    public PageInfo<UserInfo> findUser(@RequestBody Map<String, String> map) {
        System.out.println(map);
        return userInfoService.findUserInfo(map.get("user"), map.get("sex"), map.get("start"), map.get("end"), Integer.valueOf(map.get("page")), Integer.valueOf(map.get("pageSize")));
    }

    /**
     * 增加用户
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "增加用户", notes = "增加用户")
    @ApiImplicitParam(
            name = "userInfo",
            required = true,
            dataType = "userInfo",
            dataTypeClass = UserInfo.class
    )
    @RequestMapping("addUser")
    public ResponseResult addUser(@RequestBody UserInfo userInfo) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //唯一性验证
        if (userInfoService.findUserByLoginName(userInfo.getLoginName()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户登录名重复");
            return responseResult;
        }
        //给id赋值
        userInfo.setId(UID.next());
        //加密密码
        userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(), "ljs"));
        if (userInfoService.addUser(userInfo) > 0) {
            responseResult.setCode(200);
            responseResult.setSuccess("增加用户成功");
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @ApiImplicitParam(
            name = "userInfo",
            required = true,
            dataType = "userInfo",
            dataTypeClass = UserInfo.class
    )
    @RequestMapping("updateUser")
    public ResponseResult updateUser(@RequestBody UserInfo userInfo) {
        //加密密码
        userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(), "ljs"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if (userInfoService.updateUser(userInfo) > 0) {
            responseResult.setCode(200);
            responseResult.setSuccess("修改用户成功");
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 删除用户
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(
            name = "id",
            required = true,
            dataType = "long",
            dataTypeClass = Long.class
    )
    @RequestMapping("deleteUser")
    public ResponseResult deleteUser(@RequestBody Map<String, Object> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if (userInfoService.deleteUser(Long.valueOf(map.get("id").toString())) > 0) {
            responseResult.setCode(200);
            responseResult.setSuccess("删除用户成功");
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 上传图片
     *
     * @param file
     * @throws IOException
     */
    @ApiOperation(value = "上传图片", notes = "上传图片")
    @ApiImplicitParam(
            name = "file",
            required = true,
            dataType = "multipartFile",
            dataTypeClass = MultipartFile.class
    )
    @RequestMapping("upload")
    void upload(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filePath = "D:/img/" + originalFilename;
        File fileImg = new File(filePath);
        file.transferTo(fileImg);
        //可自定义大小    实现缩略图功能
        Thumbnails.of(filePath).scale(0.25f).toFile(fileImg.getAbsolutePath() + "_25%.jpg");
    }

    /**
     * 修改用户角色
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "修改用户角色", notes = "修改用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "uid",
                    required = true,
                    dataType = "long",
                    dataTypeClass = Long.class
            ),
            @ApiImplicitParam(
                    name = "rid",
                    required = true,
                    dataType = "long",
                    dataTypeClass = Long.class
            )
    })
    @RequestMapping("editRole")
    public ResponseResult editRole(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if (userInfoService.editRole(Long.valueOf(map.get("uid").toString()), Long.valueOf(map.get("rid").toString())) > 0) {
            responseResult.setCode(200);
            responseResult.setSuccess("修改用户角色成功");
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

//    @RequestMapping("uploadExcel")
//    public void uploadExcel() throws IOException {
//        list = null;
//        File file = new File("D:\\biao\\POI测试.xls");
//        UserEcxcelUtils.importExcel(file,list);
//        list.forEach(userInfo -> {
//            System.out.println(userInfo);
//        });
//    }

}
