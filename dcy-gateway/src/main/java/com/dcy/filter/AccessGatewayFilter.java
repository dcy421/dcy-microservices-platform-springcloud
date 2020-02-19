package com.dcy.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dcy.common.constant.CommonConstant;
import com.dcy.common.model.ResponseData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description: 权限验证
 * @Date: 2020-02-16 08:42
 */
@Component
public class AccessGatewayFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 不拦截url
     */
    @Value("${ignored}")
    private List<String> ignored;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        ServerHttpRequest.Builder mutate = request.mutate();
        ServerHttpRequest build = mutate.build();
        // 不进行拦截的地址
        String url = getUrl(requestUri);
        for (String igUrl : ignored) {
            if (antPathMatcher.match(igUrl, url)) {
                return chain.filter(exchange.mutate().request(build).build());
            }
        }
        String authorization = request.getHeaders().getFirst(CommonConstant.TOKEN_HEADER);
        if (StrUtil.isNotBlank(authorization)) {
            authorization = authorization.replaceFirst(CommonConstant.BEARER_TYPE, "");
            // 解析token
            DefaultClaims body = (DefaultClaims) Jwts.parser().setSigningKey(CommonConstant.SIGNING_KEY.getBytes()).parseClaimsJws(authorization).getBody();
            if (System.currentTimeMillis() > body.getExpiration().getTime()) {
                return getVoidMono(exchange, ResponseData.error(3000,"token已经过期了"));
            }
            // 获取用户对象
            Map<String, Object> map = body.get(CommonConstant.USER_INFO, Map.class);
            String userId = MapUtil.getStr(map, "userId");
            String username = MapUtil.getStr(map, "username");
            // 根据用户id 获取权限
            List<Map<String, Object>> moduleResourcesList = (List<Map<String, Object>>) redisTemplate.opsForValue().get(CommonConstant.REDIS_USER_MODULE_LIST_KEY + userId);
            for (Map<String, Object> moduleResources : moduleResourcesList) {
                // 判断是否符合url 和 method
                if (antPathMatcher.match(MapUtil.getStr(moduleResources, "modulePath"), url) && request.getMethod().matches(MapUtil.getStr(moduleResources, "httpMethod"))) {
                    ServerHttpRequest newRequest = request.mutate().headers(httpHeaders -> {
                        httpHeaders.add(CommonConstant.CONTEXT_KEY_USER_ID, userId);
                        httpHeaders.add(CommonConstant.CONTEXT_KEY_USERNAME, username);
                    }).build();
                    return chain.filter(exchange.mutate().request(newRequest).build());
                }
            }
            return getVoidMono(exchange, ResponseData.error("没有权限"));
        } else {
            return getVoidMono(exchange, ResponseData.error("没有token"));
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }

    /**
     * 获取请求中的url
     *
     * @param url
     * @return
     */
    private String getUrl(String url) {
        //获取当前访问url
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            return url.substring(0, firstQuestionMarkIndex);
        }
        return url;
    }

    /**
     * 网关抛异常
     *
     * @param responseData
     */
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, ResponseData<String> responseData) {
        // 没有权限
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        serverWebExchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(JSON.toJSONBytes(responseData));
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

}
