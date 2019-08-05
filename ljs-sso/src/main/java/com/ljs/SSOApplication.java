package com.ljs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName SSOApplication
 * @Description: TODO
 * @Author 小松
 * @Date 2019/8/5
 **/
@SpringBootApplication
@RestController
public class SSOApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class);
    }

    @RequestMapping("testHealth")
    public void testHealth(){
        System.out.println("========SSO健康检查=======");
    }

}
