package com.dcy.handler;

import com.dcy.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-18 11:20
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {


    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {

        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
                .ifPresent(originalUrls -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrls));

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ResponseData.error(9999, "调用失败")));
    }
}
