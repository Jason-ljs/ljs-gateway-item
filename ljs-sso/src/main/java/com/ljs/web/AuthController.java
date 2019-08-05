package com.ljs.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ljs.exception.LoginException;
import com.ljs.jwt.JWTUtils;
import com.ljs.pojo.ResponseResult;
import com.ljs.pojo.entity.UserInfo;
import com.ljs.radom.VerifyCodeUtils;
import com.ljs.service.UserService;
import com.ljs.utils.MD5;
import com.ljs.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AuthController
 * @Description: 用户认证类
 * @Author 小松
 * @Date 2019/8/5
 **/
@Controller
public class AuthController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 登陆操作
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String,Object> map) throws LoginException {
        ResponseResult responseResult=ResponseResult.getResponseResult();
        //获取生成的验证码
        String code = redisTemplate.opsForValue().get(map.get("codekey").toString());
        //获取传入的验证码是否是生成后存在redis中的验证码
        if(code==null||!code.equals(map.get("code").toString()) ){
            responseResult.setCode(500);
            responseResult.setError("验证码错误,请重新刷新页面登陆");
            return responseResult;
        }
        //进行用户密码的校验
        if(map!=null&&map.get("loginname")!=null){
            //根据用户名获取用户信息
            UserInfo user = userService.getUserByLogin(map.get("loginname").toString());
            if(user!=null){
                //比对密码
                String password = MD5.encryptPassword(map.get("password").toString(), "lcg");
                if(user.getPassword().equals(password)){

                    //将用户信息转存为JSON串
                    String userinfo = JSON.toJSONString(user);

                    //将用户信息使用JWt进行加密，将加密信息作为票据
                    String token = JWTUtils.generateToken(userinfo);

                    //将加密信息存入statuInfo
                    responseResult.setToken(token);

                    //将生成的token存储到redis库
                    redisTemplate.opsForValue().set("USERINFO"+user.getId().toString(),token);
                    //将该用户的数据访问权限信息存入缓存中
                    redisTemplate.opsForHash().putAll("USERDATAAUTH"+user.getId().toString(),user.getAuthmap());

                    //设置token过期 30分钟
                    redisTemplate.expire("USERINFO"+user.getId().toString(),600,TimeUnit.SECONDS);
                    //设置返回值
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    //设置成功信息
                    responseResult.setSuccess("登陆成功！^_^");

                    return responseResult;
                }else{
                    throw new LoginException("用户名或密码错误");
                }
            }else{
                throw new LoginException("用户名或密码错误");
            }
        }else{
            throw new LoginException("用户名或密码错误");
        }

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
     * @return
     */
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        //生成一个长度是5的随机字符串
        String code= VerifyCodeUtils.generateVerifyCode (5);
        ResponseResult responseResult=ResponseResult.getResponseResult ();
        responseResult.setResult ( code );
        String uidCode="CODE"+ UID.getUUID16();
        //将生成的随机字符串标识后存入redis
        redisTemplate.opsForValue().set(uidCode,code);
        //设置过期时间
        redisTemplate.expire(uidCode,1,TimeUnit.MINUTES);
        //回写cookie
        Cookie cookie=new Cookie("authcode",uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        return responseResult;
    }

    /**
     * 手动加载密码
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(MD5.encryptPassword("123456","lcg"));

        /*Map<String,String> map=new HashMap<>();
        map.put("id","645564654");
        String token = JWTUtils.generateToken(JSON.toJSONString(map));

        System.out.println(token);*/

        JSONObject jsonObject = JWTUtils.decodeJwtTocken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNTY0OTEzMzIzMzU4LCJleHAiOjE1NjQ5MTMzODMsInVzZXJpbmZvIjoie1wiaWRcIjpcIjY0NTU2NDY1NFwifSJ9.iT-NmNBkbjK29t4DLtyJvsAwp770QyYkUpEGB-Lmy-xDVH2NWUtPqQJmovV7PZV46IGPVUMvYMOAaEhbJ6voaA");

        System.out.println(jsonObject.get("id"));

    }




}
