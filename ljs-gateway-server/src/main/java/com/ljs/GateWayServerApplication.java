package com.ljs;

import com.ljs.config.MyReslover;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName GateWayServerApplication
 * @Description: TODO
 * @Author 小松
 * @Date 2019/8/4
 **/
@RestController
@SpringBootApplication
public class GateWayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayServerApplication.class);
    }

    @RequestMapping("testHealth")
    public void testHealth(){
        System.out.println("========网关服务端健康检查=======");
    }

    @Bean
    public MyReslover getMyReslover(){
        return new MyReslover();
    }

}
