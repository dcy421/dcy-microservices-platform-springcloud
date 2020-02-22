//package com.dcy.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
//import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.core.script.RedisScript;
//import org.springframework.validation.Validator;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * @Author：dcy
// * @Description:
// * @Date: 2020-02-16 13:54
// */
//@Configuration
//public class DefaultRedisRateLimiter extends RedisRateLimiter {
//
//    Config getDefaultConfig() {
//        return super.getConfig().get("defaultFilters");
//    }
//
//    public DefaultRedisRateLimiter(ReactiveRedisTemplate<String, String> redisTemplate, RedisScript<List<Long>> script, Validator validator) {
//        super(redisTemplate, script, validator);
//    }
//
//
//    @Override
//    public Mono<Response> isAllowed(String routeId, String id) {
//        if (null == super.getConfig().get(routeId)) {
//            getConfig().put(routeId, getDefaultConfig());
//        }
//        return super.isAllowed(routeId, id);
//    }
//
//}
//
