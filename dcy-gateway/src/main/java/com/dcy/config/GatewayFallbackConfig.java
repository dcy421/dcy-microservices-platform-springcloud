package com.dcy.config;

import com.dcy.handler.HystrixFallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020-02-18 11:20
 */
@Configuration
public class GatewayFallbackConfig {
    @Autowired
    private HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/defaultfallback")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
    }

}
