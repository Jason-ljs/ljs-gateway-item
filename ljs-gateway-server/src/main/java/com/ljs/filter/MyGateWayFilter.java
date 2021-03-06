package com.ljs.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName GateWayFilter
 * @Description: GateWay 过滤器
 * @Author 小松
 * @Date 2019/8/5
 **/
public class MyGateWayFilter implements GatewayFilter,Ordered {

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("====这是===MyGateWayFilter   MyGateWayFilter========");
        return chain.filter(exchange);
    }

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
