package com.ljs.exception;

/**
 * @ClassName JwtUtilt
 * @Description: 自定义的登录异常类
 * @Author 小松
 * @Date 2019/8/5
 **/
public class LoginException extends Exception {

    public LoginException(String message){
        super(message);
    }

}
