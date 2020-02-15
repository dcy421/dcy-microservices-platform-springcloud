package com.dcy.filter;

import cn.hutool.core.util.IdUtil;
import com.dcy.common.constant.CommonConstant;
import com.dcy.log.properties.TraceProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @Author：dcy
 * @Description: 生成traceId并通过header传递给下游服务
 * @Date: 2019/9/19 9:40
 */
@Component
public class TraceFilter implements GlobalFilter, Ordered {


    @Autowired
    private TraceProperties traceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest newRequest = request.mutate().headers(httpHeaders -> {
            if (traceProperties.getEnable()){
                //链路追踪id
                String traceId = IdUtil.fastSimpleUUID();
                MDC.put(CommonConstant.LOG_TRACE_ID, traceId);
                httpHeaders.add(CommonConstant.TRACE_ID_HEADER, traceId);
            }
        }).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
