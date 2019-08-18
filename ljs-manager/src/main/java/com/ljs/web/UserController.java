package com.ljs.web;

import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.ljs.config.UserEcxcelUtils;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.UserInfo;
import com.ljs.service.UserInfoService;
import com.ljs.utils.MD5;
import com.ljs.utils.UID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author 小松
 * @Date 2019/8/7
 **/
@RestController
@Api(value = "用户相关业务" ,tags = "用户操作相关")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

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
        if (userInfoService.findUserByTel(userInfo.getTel()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户手机号重复");
            return responseResult;
        }
        if (userInfoService.findUserByEmail(userInfo.getEmail()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户邮箱账号重复");
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
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //唯一性验证
        if (userInfoService.findUserByLoginName(userInfo.getLoginName()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户登录名重复");
            return responseResult;
        }
        if (userInfoService.findUserByTel(userInfo.getTel()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户手机号重复");
            return responseResult;
        }
        if (userInfoService.findUserByEmail(userInfo.getEmail()) != null) {
            responseResult.setCode(500);
            responseResult.setError("用户邮箱账号重复");
            return responseResult;
        }
        //加密密码
        userInfo.setPassword(MD5.encryptPassword(userInfo.getPassword(), "ljs"));
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
        Thumbnails.of(filePath).scale(0.25f).toFile(fileImg.getAbsolutePath() + "_sl.jpg");
    }

    @RequestMapping("test")
    public void test() throws FileNotFoundException {
        File file = new File("D:\\IMG\\此电脑.png_sl.jpg");

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(

                new FileInputStream(file), file.length(), "png", null);

        System.out.println(storePath.getFullPath());

        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());

        System.out.println(path.substring(0,path.lastIndexOf("_")));

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

    /**
     * 给折线图赋值
     * @return
     */
    @RequestMapping("opinionData")
    public Map<String,Object> opinionData() throws ParseException {
        //获取2019-开头的所有键
        Set<String> keys = redisTemplate.keys("2019-*");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Set<Date> treeSet = new TreeSet();
        for (String str : keys) {
            treeSet.add(simpleDateFormat.parse(str));
        }
        List<String> xList = new ArrayList<>();
        List<String> yList = new ArrayList<>();
        for (Date key : treeSet) {
            String value = redisTemplate.opsForValue().get(simpleDateFormat.format(key));
            xList.add(simpleDateFormat.format(key));
            yList.add(value);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("xList",xList);
        map.put("yList",yList);
        return map;
    }

    /**
     * 批量添加
     * @param file
     * @throws IOException
     */
    @RequestMapping("uploadExcel")
    public void uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<UserInfo> list = UserEcxcelUtils.importExcel(file.getInputStream());
        list.forEach(userInfo -> {
            userInfoService.addUser(userInfo);
        });
    }

}
