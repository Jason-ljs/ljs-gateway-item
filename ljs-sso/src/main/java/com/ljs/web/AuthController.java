package com.ljs.web;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.ljs.exception.LoginException;
import com.ljs.jwt.JWTUtils;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.UserInfo;
import com.ljs.radom.VerifyCodeUtils;
import com.ljs.service.UserService;
import com.ljs.utils.MD5;
import com.ljs.utils.MailUtil;
import com.ljs.utils.SmsUtil;
import com.ljs.utils.UID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AuthController
 * @Description: 用户认证类
 * @Author 小松
 * @Date 2019/8/5
 **/
@Api(value = "用户登录相关业务", tags = "用户登录相关")
@Controller
public class AuthController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 登陆操作
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "登录业务", notes = "登录业务")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "codekey",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "loginname",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            ),
            @ApiImplicitParam(
                    name = "password",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String, Object> map) throws LoginException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //获取生成的验证码
        String ss = map.get("codekey").toString();
        String code = redisTemplate.opsForValue().get(map.get("codekey").toString());
        //获取传入的验证码是否是生成后存在redis中的验证码
        if (code == null || !code.equals(map.get("code").toString())) {
            responseResult.setCode(500);
            responseResult.setError("验证码错误,请重新刷新页面登陆");
            return responseResult;
        }
        //进行用户密码的校验
        if (map != null && map.get("loginname") != null) {
            //根据用户名获取用户信息
            UserInfo user = userService.getUserByLogin(map.get("loginname").toString());
            if (user != null) {
                //比对密码
                String password = MD5.encryptPassword(map.get("password").toString(), "ljs");
                if (user.getPassword().equals(password)) {

                    //将用户信息转存为JSON串
                    String userinfo = JSON.toJSONString(user);

                    //将用户信息使用JWt进行加密，将加密信息作为票据
                    String token = JWTUtils.generateToken(userinfo);

                    System.out.println(token);

                    //将加密信息存入statuInfo
                    responseResult.setToken(token);

                    //将生成的token存储到redis库
                    redisTemplate.opsForValue().set("USERINFO" + user.getId().toString(), token);
                    //将该用户的数据访问权限信息存入缓存中
                    redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId().toString(), user.getAuthmap());

                    //设置token过期 30分钟
                    redisTemplate.expire("USERINFO" + user.getId().toString(), 600, TimeUnit.SECONDS);

                    //每日用户活跃量折线图使用
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String format = simpleDateFormat.format(new Date());
                    String hasKey = user.getLoginName() + format;
                    if (!redisTemplate.hasKey(hasKey)) {
                        redisTemplate.opsForValue().set(hasKey, "");
                        redisTemplate.expire(hasKey, 1, TimeUnit.DAYS);
                        if (redisTemplate.hasKey(format)) {
                            Integer integer = Integer.valueOf(redisTemplate.opsForValue().get(format)) + 1;
                            redisTemplate.opsForValue().set(format, integer.toString());
                        } else {
                            redisTemplate.opsForValue().set(format, "1");
                        }
                        redisTemplate.expire(format, 30, TimeUnit.DAYS);
                    }

                    //设置返回值
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    //设置成功信息
                    responseResult.setSuccess("登陆成功！^_^");
                    return responseResult;
                } else {
                    responseResult.setError("用户名或密码错误");
                    return responseResult;
                }
            } else {
                responseResult.setError("用户名或密码错误");
                return responseResult;
            }
        } else {
            responseResult.setError("用户名或密码错误");
            return responseResult;
        }

    }

    /**
     * 短信验证登录
     *
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("smsLogin")
    public ResponseResult smsLogin(@RequestBody Map<String, String> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        UserInfo user = userService.getUserByTel(map.get("phoneNumber"));
        if (user != null) {
            String code = map.get("code");
            String redisCode = redisTemplate.opsForValue().get("CODE" + code);
            if (code.equals(redisCode)) {
                //将用户信息转存为JSON串
                String userinfo = JSON.toJSONString(user);

                //将用户信息使用JWt进行加密，将加密信息作为票据
                String token = JWTUtils.generateToken(userinfo);

                System.out.println(token);

                //将加密信息存入statuInfo
                responseResult.setToken(token);

                //将生成的token存储到redis库
                redisTemplate.opsForValue().set("USERINFO" + user.getId().toString(), token);
                //将该用户的数据访问权限信息存入缓存中
                redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId().toString(), user.getAuthmap());

                //设置token过期 30分钟
                redisTemplate.expire("USERINFO" + user.getId().toString(), 600, TimeUnit.SECONDS);

                //每日用户活跃量折线图使用
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(new Date());
                String hasKey = user.getLoginName() + format;
                if (!redisTemplate.hasKey(hasKey)) {
                    redisTemplate.opsForValue().set(hasKey, "");
                    redisTemplate.expire(hasKey, 1, TimeUnit.DAYS);
                    if (redisTemplate.hasKey(format)) {
                        Integer integer = Integer.valueOf(redisTemplate.opsForValue().get(format)) + 1;
                        redisTemplate.opsForValue().set(format, integer.toString());
                    } else {
                        redisTemplate.opsForValue().set(format, "1");
                    }
                    redisTemplate.expire(format, 30, TimeUnit.DAYS);
                }

                //设置返回值
                responseResult.setResult(user);
                responseResult.setCode(200);
                //设置成功信息
                responseResult.setSuccess("登陆成功！^_^");
                return responseResult;
            } else {
                responseResult.setError("验证码超时!");
                return responseResult;
            }
        } else {
            responseResult.setError("手机号未注册!");
            return responseResult;
        }
    }

    /**
     * 获取短信验证码
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("getSmsCode")
    public ResponseResult getSmsCode(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> map) throws ClientException {
        UserInfo user = userService.getUserByTel(map.get("phoneNumber"));
        System.out.println(user);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if (user != null) {
            //生成一个长度为4的随机字符串
            String code = SmsUtil.getCode();
            String status = SmsUtil.sendSms(map.get("phoneNumber"), code);
            if (status.equals("OK")) {
                Cookie[] cookies = request.getCookies();
                responseResult.setResult(code);
                String uidCode = "CODE" + code;
                //将生成的随机字符串标识后存入redis
                redisTemplate.opsForValue().set(uidCode, code);
                //设置过期时间
                redisTemplate.expire(uidCode, 50, TimeUnit.MINUTES);
                //回写cookie
                Cookie cookie = new Cookie("authcode", uidCode);
                cookie.setPath("/");
                cookie.setDomain("localhost");
                response.addCookie(cookie);
                responseResult.setCode(200);
                return responseResult;
            } else {
                responseResult.setError("验证码发送失败");
                return responseResult;
            }
        } else {
            responseResult.setError("手机号未注册！");
            return responseResult;
        }

    }


    @ApiOperation(value = "退出业务", notes = "退出业务")
    @ApiImplicitParam(
            name = "id",
            required = true,
            dataType = "long",
            dataTypeClass = Long.class
    )
    @ResponseBody
    @RequestMapping("loginout")
    public ResponseResult loginout(Integer id) {
        //将存储到redis库的token删除
        redisTemplate.delete("USERINFO" + id);
        //将该用户的数据访问权限信息从redis库删除
        redisTemplate.delete("USERDATAAUTH" + id);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setSuccess("ok");
        return responseResult;
    }


    /**
     * 检测tocken是否是超时，检查Token是否还可用
     * @param map
     * @return
     *//*
    @RequestMapping("toCheckLogin")
    @ResponseBody
    public ResponseResult toCheckLogin(HttpServletRequest request,HttpServletResponse response){

        String token = request.getHeader("token");
        JSONObject jsonObject=null;
        try{
            jsonObject = JWTUtils.decodeJwtTocken(token);
        }catch (Exception e){
             e.printStackTrace();

        }

    }*/

    /**
     * 获取滑动验证的验证码
     *
     * @return
     */
    @ApiOperation(value = "获取滑动验证的验证码", notes = "获取滑动验证的验证码")
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        //生成一个长度为5的随机字符串
        String code = VerifyCodeUtils.generateVerifyCode(5);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(code);
        String uidCode = "CODE" + UID.getUUID16();
        //将生成的随机字符串标识后存入redis
        redisTemplate.opsForValue().set(uidCode, code);
        //设置过期时间
        redisTemplate.expire(uidCode, 1, TimeUnit.MINUTES);
        //回写cookie
        Cookie cookie = new Cookie("authcode", uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return responseResult;
    }

    /**
     * 手动加载密码
     * @param
     */
    /*public static void main(String[] args) {

        System.out.println(MD5.encryptPassword("123456","lcg"));

        *//*Map<String,String> map=new HashMap<>();
        map.put("id","645564654");
        String token = JWTUtils.generateToken(JSON.toJSONString(map));

        System.out.println(token);*//*

        JSONObject jsonObject = JWTUtils.decodeJwtTocken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNTY0OTEzMzIzMzU4LCJleHAiOjE1NjQ5MTMzODMsInVzZXJpbmZvIjoie1wiaWRcIjpcIjY0NTU2NDY1NFwifSJ9.iT-NmNBkbjK29t4DLtyJvsAwp770QyYkUpEGB-Lmy-xDVH2NWUtPqQJmovV7PZV46IGPVUMvYMOAaEhbJ6voaA");

        System.out.println(jsonObject.get("id"));

    }*/

    /**
     * 发送邮件
     *
     * @param map
     * @return
     */
    @RequestMapping("sendMail")
    @ResponseBody
    public ResponseResult sendMail(@RequestBody Map<String, String> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        UserInfo byLogin = userService.getUserByLogin(map.get("account"));
        UserInfo byTel = userService.getUserByTel(map.get("account"));
        UserInfo byEmail = userService.getUserByEmail(map.get("account"));
            String code = UUID.randomUUID().toString();
            if (byLogin != null) {
                new Thread(new MailUtil(byLogin.getEmail(), code)).start();
                userService.updateCodeById(byLogin.getId(), code);
                redisTemplate.opsForValue().set(code, "");
                redisTemplate.expire(code, 60, TimeUnit.MINUTES);
                responseResult.setCode(200);
            } else if (byTel != null) {
                new Thread(new MailUtil(byTel.getEmail(), code)).start();
                userService.updateCodeById(byTel.getId(), code);
                redisTemplate.opsForValue().set(code, "");
                redisTemplate.expire(code, 60, TimeUnit.MINUTES);
                responseResult.setCode(200);
            } else if (byEmail != null){
                new Thread(new MailUtil(byEmail.getEmail(), code)).start();
                userService.updateCodeById(byEmail.getId(), code);
                redisTemplate.opsForValue().set(code, "");
                redisTemplate.expire(code, 60, TimeUnit.MINUTES);
                responseResult.setCode(200);
            } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @RequestMapping("resetPwd")
    @ResponseBody
    public ResponseResult resetPwd(@RequestBody Map<String, String> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        UserInfo byCode = userService.findUserByCode(map.get("code"));
        if (redisTemplate.hasKey(map.get("code"))) {
            userService.updatePasswordByCode(MD5.encryptPassword(map.get("password"),"ljs"),map.get("code"));
            responseResult.setCode(200);
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }

}
