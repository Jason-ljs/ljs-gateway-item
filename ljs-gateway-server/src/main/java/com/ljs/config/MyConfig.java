package com.ljs.config;


import com.ljs.filter.MyGateWayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MyConfig
 * @Description: 自定义过滤器
 * @Author 小松
 * @Date 2019/8/5
 **/
@Configuration
public class MyConfig {

    /**
     * 在路由中添加自定义的过滤器
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder route = routeLocatorBuilder.routes().route(
                r -> r.path("/api/client/**")
                        .filters(f -> f.stripPrefix(2).filter(getMyFilter()))//在路由中添加自定义的过滤器
                        .uri("lb://gateway-client")
                        .order(100)
                        .id("gateway-client1")
        );
        return route.build();
    }

    @Bean
    public MyGateWayFilter getMyFilter(){
        return new MyGateWayFilter();
    }



}
