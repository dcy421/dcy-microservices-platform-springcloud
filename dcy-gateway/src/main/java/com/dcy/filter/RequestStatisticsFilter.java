package com.dcy.filter;

import cn.hutool.core.collection.CollUtil;
import com.dcy.log.monitor.PointUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 请求统计分析埋点过滤器
 * @Date: 2019/9/19 11:10
 */
@Component
public class RequestStatisticsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> userAgents = request.getHeaders().get("User-Agent");
        if (CollUtil.isNotEmpty(userAgents)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(CollUtil.getFirst(userAgents));
            //埋点
            PointUtil.info("0", "request-statistics",
                    "ip=" + request.getHeaders().getHost()
                            + "&browser=" + userAgent.getBrowser()
                            + "&operatingSystem=" + userAgent.getOperatingSystem());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
