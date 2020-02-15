package com.dcy;

import org.minbox.framework.api.boot.autoconfigure.swagger.annotation.EnableApiBootSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dcy
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableApiBootSwagger
@EnableCaching
@MapperScan(basePackages = {"com.dcy.mapper"})
public class AdminCenterProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminCenterProviderApplication.class, args);
    }

}
